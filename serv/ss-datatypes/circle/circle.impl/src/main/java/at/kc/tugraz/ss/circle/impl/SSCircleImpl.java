/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package at.kc.tugraz.ss.circle.impl;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.circle.api.SSCircleClientI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.impl.fct.activity.SSCircleActivityFct;
import at.kc.tugraz.ss.circle.impl.fct.misc.SSCircleMiscFct;
import at.kc.tugraz.ss.circle.impl.fct.sql.SSCircleSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityToPrivCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityToPubCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUserCanPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSCircleEntityPublicSetRet;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErrE;

public class SSCircleImpl extends SSServImplWithDBA implements SSCircleClientI, SSCircleServerI{
  
  private final SSCircleSQLFct sqlFct;

  public SSCircleImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    super(conf, null, dbSQL);
    
    this.sqlFct = new SSCircleSQLFct(dbSQL);
  }
  
  @Override
  public void circleCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }
    
    final SSUri result = circleCreate(parA);
    
    sSCon.writeRetFullToClient(SSCircleCreateRet.get(result, parA.op));
    
    SSCircleActivityFct.createCircle(new SSCircleCreatePar(parA), result);
  }
  
  @Override
  public SSUri circleCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCircleCreatePar par        = new SSCircleCreatePar(parA);
      final SSUri             circleUri  = SSServCaller.vocURICreate();
      
      if(par.withUserRestriction){
        SSServCallerU.canUserEditEntities (par.user, par.entities);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        circleUri,
        SSEntityE.circle, 
        par.label, 
        par.description, 
        null, 
        false);
      
      sqlFct.addCircle(
        circleUri, 
        SSCircleE.group, 
        false);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri, 
        par.user);
      
      if(!par.entities.isEmpty()){
      
        SSServCaller.circleEntitiesAdd(
          par.user, 
          circleUri, 
          par.entities, 
          par.invokeEntityHandlers, 
          false, 
          false);
      }      
      
      if(!par.users.isEmpty()){
        
        SSServCaller.circleUsersAdd(
          par.user, 
          circleUri,   
          par.users,  
          false,
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return circleCreate(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleUsersAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

    final SSUri result = circleUsersAdd(parA);
    
    sSCon.writeRetFullToClient(SSCircleUsersAddRet.get(result, parA.op));
    
    SSCircleActivityFct.addUsersToCircle(new SSCircleUsersAddPar(parA));
  }
  
  @Override
  public SSUri circleUsersAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCircleUsersAddPar par = new SSCircleUsersAddPar(parA);
      
      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct, par.user, par.circle);
      }
      
      SSServCallerU.checkWhetherUsersAreUsers(par.users);
        
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.users){
        sqlFct.addUserToCircleIfNotExists(par.circle, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return circleUsersAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleEntitiesAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

    sSCon.writeRetFullToClient(SSCircleEntitiesAddRet.get(circleEntitiesAdd(parA), parA.op));
    
    SSCircleActivityFct.addEntitiesToCircle(new SSCircleEntitiesAddPar(parA));
  }
  
  @Override
  public SSUri circleEntitiesAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCircleEntitiesAddPar par = new SSCircleEntitiesAddPar(parA);
      
      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct,   par.user, par.circle);
        SSServCallerU.canUserEditEntities                     (par.user, par.entities);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCallerU.addEntities(
        par.user,
        par.entities);
      
      for(SSUri entityUri : par.entities){
        sqlFct.addEntityToCircleIfNotExists(par.circle, entityUri);
      }
      
      if(par.invokeEntityHandlers){
        
        SSCircleMiscFct.shareEntityWithCircleByHandlers(
          par.user, 
          par.entities,
          par.circle);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return circleEntitiesAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSCircleE circleMostOpenCircleTypeGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCircleMostOpenCircleTypeGetPar par                = new SSCircleMostOpenCircleTypeGetPar(parA);
      SSCircleE                              mostOpenCircleType = SSCircleE.priv;
      
      for(SSCircleE circleType : SSServCaller.circleTypesGet(par.user, par.forUser, par.entity, par.withUserRestriction)){
        
        switch(circleType){
          
          case pub:  return SSCircleE.pub;
          case priv: continue;
          default:   mostOpenCircleType = SSCircleE.group;
        }
      }
      
      return mostOpenCircleType;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCircleE> circleTypesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSCircleTypesGetPar par = new SSCircleTypesGetPar(parA);
      
      if(par.entity == null){
        throw new Exception("entity to retrieve circle types for is null");
      }
      
      if(
        par.withUserRestriction &&
        par.forUser != null &&
        !SSStrU.equals(par.forUser, par.user)){
        throw new Exception("user cannot retrieve circle types for other user");
      }
      
      if(par.forUser == null){
        return sqlFct.getCircleTypesForEntity(par.entity);
      }else{
        return sqlFct.getCircleTypesCommonForUserAndEntity(par.forUser, par.entity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

    sSCon.writeRetFullToClient(SSCircleGetRet.get(circleGet(parA), parA.op));
  }    
  
  @Override
  public SSEntityCircle circleGet(final SSServPar parA) throws Exception{
    
    try{
      final SSCircleGetPar  par = new SSCircleGetPar(parA);
      
      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(
          par.withSystemCircles ||
          sqlFct.isSystemCircle(par.circle)){
          
          throw new Exception("user cannot access system circle");
        }
        
        SSServCallerU.canUserReadEntity(par.forUser, par.circle);
      }
      
      return sqlFct.getCircle(par.circle, true, true, true);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circlesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

    sSCon.writeRetFullToClient(SSCirclesGetRet.get(circlesGet(parA), parA.op));
  }
  
  @Override
  public List<SSEntityCircle> circlesGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCirclesGetPar                 par               = new SSCirclesGetPar(parA);
      final List<SSEntityCircle>            circles           = new ArrayList<>();
      final List<SSUri>                     circleUris        = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(par.withSystemCircles){
          throw new Exception("user cannot access system circles");
        }
        
        if(par.entity != null){
          SSServCallerU.canUserReadEntity(par.forUser, par.entity);
        }
      }
      
      if(!SSObjU.isNull(par.forUser, par.entity)){
        
        for(SSEntityCircle circle : 
          sqlFct.getCirclesCommonForUserAndEntity(
            par.forUser,
            par.entity,
            par.withSystemCircles)){
          
          circleUris.add(circle.id);
        }
        
      }else{
        
        if(
          par.forUser == null &&
          par.entity  == null){
          
          circleUris.addAll(sqlFct.getCircleURIs(par.withSystemCircles));
        }else{
          
          if(par.forUser != null){
            circleUris.addAll(sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles));
          }
          
          if(par.entity != null){
            circleUris.addAll(sqlFct.getCircleURIsForEntity(par.entity, par.withSystemCircles));
          }
        }
      }
      
      if(par.withUserRestriction){
        
        for(SSUri circleUri : circleUris){
          
          try{
            SSServCallerU.canUserReadEntity(par.forUser, circleUri);
          }catch(Exception error){
            SSServErrReg.reset();
            continue;
          }
          
          circles.add(
            SSServCaller.circleGet(
              par.forUser,
              null,
              circleUri,
              par.withSystemCircles,
              true));
        }
      }else{
        
        for(SSUri circleUri : circleUris){
          
          circles.add(
            SSServCaller.circleGet(
              par.user,
              null,
              circleUri,
              par.withSystemCircles,
              false));
        }
      }

      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleEntitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }
    
    sSCon.writeRetFullToClient(SSCircleEntitiesGetRet.get(circleEntitiesGet(parA), parA.op));
  }
  
  @Override
  public List<SSEntity> circleEntitiesGet(final SSServPar parA) throws Exception{
    
    //TODO to be refactored exhaustively (e.g. introduce circle parameter and user par.withUserRestriction together with par.forUser)
    try{
      final SSCircleEntitiesGetPar par      = new SSCircleEntitiesGetPar(parA);
      final List<SSEntity>         entities = new ArrayList<>();

      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
      }
      
      for(SSEntityCircle circle : SSServCaller.circlesGet(par.user, par.forUser, null, true, false)){

        for(SSEntity entity : circle.entities){
          
          try{
            entities.add(SSServCaller.entityUserGet(par.user, entity.id, par.forUser, false));
          }catch(Exception error){
            
            if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
              SSServErrReg.reset();
              continue;
            }
            
            throw error;
          }
        }
      }
      
      SSStrU.distinctWithoutNull2(entities);
      
      return entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circlePrivURIGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCirclePrivURIGetPar par = new SSCirclePrivURIGetPar(parA);
      final SSUri                       privCircleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      privCircleUri = SSCircleMiscFct.addOrGetPrivCircleURI(sqlFct, par.user);
      
      dbSQL.commit(parA.shouldCommit);
      
      return privCircleUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return circlePrivURIGet(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circlePubURIGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUri circleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      circleUri = SSCircleMiscFct.addOrGetPubCircleURI(sqlFct);
      
      dbSQL.commit(parA.shouldCommit);
      
      return circleUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return circlePubURIGet(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityEntityToPrivCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCircleEntityToPrivCircleAddPar par = new SSCircleEntityToPrivCircleAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user, 
        par.entity, 
        par.type, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);
      
      SSServCaller.circleEntitiesAdd(
        par.user, 
        SSCircleMiscFct.addOrGetPrivCircleURI (sqlFct, par.user), 
        SSUri.asListWithoutNullAndEmpty(par.entity), 
        false, 
        false, 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          entityEntityToPrivCircleAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void entityEntityToPubCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCircleEntityToPubCircleAddPar par = new SSCircleEntityToPubCircleAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user, 
        par.entity, 
        par.type, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);

      SSServCaller.circleEntitiesAdd(
        par.user, 
        SSCircleMiscFct.addOrGetPubCircleURI (sqlFct), 
        SSUri.asListWithoutNullAndEmpty      (par.entity), 
        false, 
        false, 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          entityEntityToPrivCircleAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity circleUserCan(final SSServPar parA) throws Exception{
   
    try{

      final SSCircleUserCanPar par    = new SSCircleUserCanPar(parA);
      final SSEntity           entity;
      
      try{
        entity = SSServCaller.entityGet(par.entity);
      }catch(Exception error){
        
        if(SSServErrReg.containsErr(SSErrE.entityDoesntExist)){
          SSServErrReg.reset();
          return null;
        }
        
        throw error;
      }
      
      SSCircleMiscFct.checkWhetherUserCanForEntityType(
        sqlFct,
        par.user,
        entity,
        par.accessRight,
        par.logErr);
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, parA.logErr);
      return null;
    }
  }
  
  @Override
  public void circleEntityPublicSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCircleEntityPublicSetRet.get(circleEntityPublicSet(parA), parA.op));
    
     SSCircleActivityFct.setEntityPublic(new SSCircleEntityPublicSetPar(parA));
  }
  
  @Override 
  public SSUri circleEntityPublicSet(final SSServPar parA) throws Exception{
    
    try{

      final SSCircleEntityPublicSetPar par = new SSCircleEntityPublicSetPar(parA);
      
      SSServCallerU.canUserAllEntity(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.addEntityToCircleIfNotExists(
        SSCircleMiscFct.getPubCircleURI(sqlFct), 
        par.entity);
      
      SSCircleMiscFct.setPublicByEntityHandlers(
        par.user,        
        par.entity);

      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return circleEntityPublicSet(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleEntityUsersGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCircleEntityUsersGetRet.get(circleEntityUsersGet(parA), parA.op));
  }
  
  @Override
  public List<SSEntity> circleEntityUsersGet(final SSServPar parA) throws Exception{
    //to be integrated with withUserRestriction
    try{
      final SSCircleEntityUsersGetPar     par             = new SSCircleEntityUsersGetPar(parA);
      final List<SSUri>                   userUris        = new ArrayList<>();
      final List<SSUri>                   userCircleUris  = sqlFct.getCircleURIsForUser   (par.user, true);
      
      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entity, true)){
        
        switch(sqlFct.getTypeForCircle(circleUri)){
          
          case priv:{
            
            if(!SSStrU.contains(userCircleUris, circleUri)){
              continue;
            }
            
            if(!SSStrU.contains(userUris, par.user)){
              userUris.add(par.user);
            }
            
            break;
          }
          
          case pub:{
            
            for(SSEntity user : sqlFct.getUsersForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, user.id)){
                userUris.add(user.id);
              }
            }
            
            break;
          }
          
          case group:{
            
            if(!SSStrU.contains(userCircleUris, circleUri)){
              continue;
            }
            
            for(SSEntity user : sqlFct.getUsersForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, user.id)){
                userUris.add(user.id);
              }
            }
            
            break;
          }
        }
      }
      
      return SSServCallerU.getEntities(userUris);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleEntityShare(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCircleEntityShareRet.get(circleEntityShare(parA), parA.op));
  }
  
  @Override  
  public SSUri circleEntityShare(final SSServPar parA) throws Exception{
    
    try{
      final SSCircleEntitySharePar par = new SSCircleEntitySharePar(parA);
      
      if(
        par.users.isEmpty() &&
        par.circles.isEmpty()){
        return par.entity;
      }
      
      SSServCallerU.canUserEditEntity                            (par.user, par.entity);

      if(!par.users.isEmpty()){
        
        SSCircleMiscFct.checkWhetherUserWantsToShareWithHimself (par.user, par.users);
        SSServCallerU.checkWhetherUsersAreUsers                 (par.users);
        
        dbSQL.startTrans(par.shouldCommit);
        
        final SSUri circleUri =
          SSServCaller.circleCreate(
            par.user,
            SSUri.asListWithoutNullAndEmpty(par.entity),
            par.users,
            null,
            null,
            true,
            false,
            false, 
            false);
        
        SSCircleMiscFct.shareEntityWithUsersByHandlers(
          par.user,
          par.users,
          par.entity,
          circleUri,
          par.saveActivity);
        
        SSCircleActivityFct.shareEntityWithUsers(par, circleUri);
      }
      
      if(!par.circles.isEmpty()){

        dbSQL.startTrans(par.shouldCommit);
        
        for(SSUri circle : par.circles){
          
          SSServCaller.circleEntitiesAdd(
            par.user, 
            circle, 
            SSUri.asListWithoutNullAndEmpty(par.entity), 
            true, 
            true, 
            false);
          
//          switch(entityType){
//            case qa:
//              SSEntityMiscFct.shareByEntityHandlers(
//                par.user,
//                sqlFct.getUserURIsForCircle(circle),
//                par.entity,
//                circle,
//                false);
//              break;
//            default:break;
//          }
        }
        
        SSCircleActivityFct.shareEntityWithCircles(par);
      }
     
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return circleEntityShare(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}