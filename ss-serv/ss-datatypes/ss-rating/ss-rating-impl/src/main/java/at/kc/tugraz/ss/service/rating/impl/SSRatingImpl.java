/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
 package at.kc.tugraz.ss.service.rating.impl;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingOverallGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingUserGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingUserSetPar;
import at.kc.tugraz.ss.service.rating.api.*;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserPublicSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingOverallGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingUserGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingUserSetRet;
import at.kc.tugraz.ss.service.rating.impl.fct.sql.SSRatingSQLFct;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingDesc;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingsUserRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.List;

public class SSRatingImpl extends SSServImplWithDBA implements SSRatingClientI, SSRatingServerI, SSEntityHandlerImplI{
 
  private final SSRatingSQLFct   sqlFct;
//  private final SSRatingGraphFct graphFct;
    
  public SSRatingImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
    sqlFct   = new SSRatingSQLFct   (this);
//    graphFct = new SSRatingGraphFct (this);
  }
  
  /* SSEntityHandlerImplI */
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
    
    if(!par.removeUserRatings){
      return;
    }
    
    try{
      SSServCaller.ratingsUserRemove(par.user, par.entityUri, shouldCommit);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean setUserEntityPublic(
    final SSEntityUserPublicSetPar par, 
    final SSEntityEnum             entityType) throws Exception{

    return false;
  }
  
  @Override
  public Boolean shareUserEntity(
    final SSEntityUserSharePar par,
    final SSEntityEnum         entityType) throws Exception{
    
    return false;
  }
    
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityEnum    entityType,
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabelStr      label,
    final Long            creationTime,
    final List<SSTag>     tags, 
    final SSRatingOverall overallRating,
    final List<SSUri>     discUris, 
    final SSUri           author) throws Exception{
    
    if(!SSEntityEnum.equals(entityType, SSEntityEnum.rating)){
      return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
    }

    return SSRatingDesc.get(
        entityUri,
        label,
        creationTime,
        author);
  }
  
  /* SSRatingClientI */
  @Override
  public void ratingUserSet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSRatingUserSetRet.get(ratingUserSet(par), par.op));
    
//    saveRatingUserSetUE(par);
  }

  @Override
  public void ratingUserGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSRatingUserGetRet.get(ratingUserGet(par), par.op));
  }

  @Override
  public void ratingOverallGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSRatingOverallGetRet.get(ratingOverallGet(par), par.op));
  }
  
  /* SSRatingServerI */
  @Override
  public Boolean ratingsUserRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRatingsUserRemovePar par = new SSRatingsUserRemovePar(parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteRatingAss(par.user, par.entityUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      try{
        
        if(dbSQL.rollBack(parA)){
          return ratingsUserRemove(parA);
        }
        
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return null;
      }
    }
  }
  
  @Override
  public Boolean ratingUserSet(SSServPar parA) throws Exception {
    
    try{
      final SSRatingUserSetPar par       = new SSRatingUserSetPar(parA);
    
      if(sqlFct.hasUserRatedEntity(par.user, par.resource)){
        return true;
      }
      
      final SSUri ratingUri = sqlFct.createRatingUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        ratingUri,
        SSLabelStr.get(ratingUri.toString()),
        SSEntityEnum.rating,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.resource,
        SSLabelStr.get(SSUri.toStr(par.resource)),
        SSEntityEnum.entity,
        false);
      
      sqlFct.rateEntityByUser (ratingUri, par.user, par.resource, par.value);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      try{
        
        if(dbSQL.rollBack(parA)){
          return ratingUserSet(parA);
        }
        
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return null;
      }
    }
  }

  @Override
  public Integer ratingUserGet(SSServPar parI) throws Exception {
    
    SSRatingUserGetPar par    = new SSRatingUserGetPar(parI);
    Integer            result = 0;
    
    try{
      result = sqlFct.getUserRating(par.user, par.resource);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }

  @Override
  public SSRatingOverall ratingOverallGet(SSServPar parI) throws Exception {
    
    SSRatingOverallGetPar par     = new SSRatingOverallGetPar(parI);
    SSRatingOverall       result  = null;
    
    try{
      result = sqlFct.getOverallRating(par.resource);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }

//  private void saveRatingUserSetUE(SSServPar parA) throws Exception {
//    
//    Map<String, Object> opPars = new HashMap<String, Object>();
//    SSRatingUserSetPar par = new SSRatingUserSetPar(parA);
//    
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.eventType,    SSUEEnum.rateEntity);
//    opPars.put(SSVarU.content,      String.valueOf(par.value));
//    
//    SSServReg.callServServer(new SSServPar(SSMethU.uEAdd, opPars));
//  }
}