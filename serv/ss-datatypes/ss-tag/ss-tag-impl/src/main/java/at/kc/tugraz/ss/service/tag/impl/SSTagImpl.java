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
package at.kc.tugraz.ss.service.tag.impl;

import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddAtCreationTimePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddAtCreationTimePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserRemoveRet;
import at.kc.tugraz.ss.service.tag.impl.fct.activity.SSTagActivityFct;
import at.kc.tugraz.ss.service.tag.impl.fct.misc.SSTagMiscFct;
import at.kc.tugraz.ss.service.tag.impl.fct.sql.SSTagSQLFct;
import java.util.*;

public class SSTagImpl extends SSServImplWithDBA implements SSTagClientI, SSTagServerI, SSEntityHandlerImplI, SSEntityDescriberI{
  
  private final SSTagSQLFct   sqlFct;
  
  public SSTagImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
    sqlFct    = new SSTagSQLFct   (this);
  }
  
  @Override
  public Boolean copyUserEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return null;
  }
  
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
    
    return false;
  }  
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
    
    try{
      
      if(!removeUserTags){
        return;
      }
      
      SSServCaller.tagsUserRemove(
        userUri, 
        entityUri, 
        null, 
        null, 
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntityDescA      entityDesc) throws Exception{
    
    if(par.getTags){
      
      entityDesc.tags.addAll(
        SSStrU.toStr(
          SSServCaller.tagsUserGet(
            par.user, 
            par.user,
            SSUri.asListWithoutNullAndEmpty(par.entity), 
            new ArrayList<String>(), 
            null, 
            null)));
    }
    
    return entityDesc;
  }
    
  @Override
  public void tagEntitiesForTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSTagUserEntitiesForTagsGetRet.get(tagUserEntitiesForTagsGet(parA), parA.op));
  }

  @Override
  public List<SSUri> tagUserEntitiesForTagsGet(final SSServPar parA) throws Exception{
    
    //TODO dtheiler: use start time for this call as well
    try{
      final SSTagUserEntitiesForTagsGetPar par = new SSTagUserEntitiesForTagsGetPar(parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        throw new Exception("user cannot get resources via tags for different users");
      }
      
      if(par.space == null){
        return SSTagMiscFct.getEntitiesForTagsIfSpaceNotSet(sqlFct, par);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return SSTagMiscFct.getEntitiesForTagsIfSpaceSet(sqlFct, par, par.forUser);
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return SSTagMiscFct.getEntitiesForTagsIfSpaceSet(sqlFct, par, par.user);
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSTagAddRet.get(tagAdd(parA), parA.op));

    SSTagActivityFct.addTag(new SSTagAddPar(parA));
  }
  
  @Override
  public Boolean tagAdd(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagAddPar par       = new SSTagAddPar(parA);
      final Boolean     existsTag = sqlFct.existsTagLabel    (par.label);
      final SSUri       tagUri    = sqlFct.getOrCreateTagURI (existsTag, par.label);

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        tagUri,
        SSLabel.get(SSStrU.toStr(par.label)),
        SSEntityE.tag,
        null,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.entity,
        SSLabel.get(SSStrU.toStr(par.entity)),
        SSEntityE.entity,
        null,
        false);
      
      sqlFct.addTagAssIfNotExists(
        tagUri,
        par.user,
        par.entity,
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void tagsRemove(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSTagsUserRemoveRet.get(tagsUserRemove(parA), parA.op));
    
    SSTagActivityFct.removeTags(new SSTagsUserRemovePar(parA));
  }
  
  @Override
  public Boolean tagsUserRemove(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagsUserRemovePar par = new SSTagsUserRemovePar (parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.space    == null &&
        par.entity == null){

        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss(par.user, null, par.label, SSSpaceE.privateSpace);
        sqlFct.removeTagAsss(par.user, null, par.label, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
       if(
         par.space    != null &&
         par.entity == null){
         
         dbSQL.startTrans(par.shouldCommit);
         
         sqlFct.removeTagAsss(par.user, null, par.label, par.space);
         
         dbSQL.commit(par.shouldCommit);
         return true;
       }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss (par.user, par.entity, par.label, SSSpaceE.privateSpace);
        sqlFct.removeTagAsss (null,     par.entity, par.label, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
      
        sqlFct.removeTagAsss(null, par.entity, par.label, par.space);

        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsUserRemove(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void tagFrequsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
       
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSTagUserFrequsGetRet.get(tagUserFrequsGet(par), par.op));
  }
  
  @Override
  public List<SSTagFrequ> tagUserFrequsGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagUserFrequsGetPar par = new SSTagUserFrequsGetPar (parA);
      
      return SSTagMiscFct.getTagFrequsFromTags(
        SSServCaller.tagsUserGet(
          par.user,
          par.forUser,
          par.entities,
          SSStrU.toStrWithoutEmptyAndNull(par.labels),
          par.space,
          par.startTime),
        par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean tagAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagAddAtCreationTimePar par       = new SSTagAddAtCreationTimePar(parA);
      final Boolean                   existsTag = sqlFct.existsTagLabel   (par.label);
      final SSUri                     tagUri    = sqlFct.getOrCreateTagURI (existsTag, par.label); 

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAddAtCreationTime(
        par.user,
        tagUri,
        SSLabel.get(SSStrU.toStr(par.label)),
        par.creationTime,
        SSEntityE.tag,
        null,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.entity,
        SSLabel.get(par.entity.toString()),
        SSEntityE.entity,
        null,
        false);
      
      sqlFct.addTagAssIfNotExists(
        tagUri, 
        par.user, 
        par.entity, 
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagAddAtCreationTime(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean tagsAdd(final SSServPar parA) throws Exception {
    
    try{

      final SSTagsAddPar par    = new SSTagsAddPar(parA);
      
      for(SSTagLabel tagLabel : par.labels) {
        
        SSServCaller.tagAdd(
          par.user, 
          par.entity, 
          SSStrU.toStr(tagLabel), 
          par.space, 
          par.shouldCommit);
      }
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean tagsAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{

      final SSTagsAddAtCreationTimePar par    = new SSTagsAddAtCreationTimePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSTagLabel tagString : par.labels) {
       
        SSServCaller.tagAddAtCreationTime(
          par.user, 
          par.entity, 
          SSStrU.toStr(tagString), 
          par.space, 
          par.creationTime, 
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsAddAtCreationTime(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean tagsRemove(final SSServPar parA) throws Exception{
  
    try{
      
      final SSTagsRemovePar par = new SSTagsRemovePar (parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeTagAsss (
        par.forUser, 
        par.entity, 
        par.label, 
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsRemove(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSTagsUserGetRet.get(tagsUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSTag> tagsUserGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagsUserGetPar par       = new SSTagsUserGetPar (parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        throw new Exception("user cannot get tags for a different user");
      }
      
      if(par.space == null){
        return SSTagMiscFct.getTagsIfSpaceNotSet(sqlFct, par);
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return SSTagMiscFct.getTagsIfSpaceSet(sqlFct, par, par.user);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return SSTagMiscFct.getTagsIfSpaceSet(sqlFct, par, par.forUser);
      }
      
      throw new Exception("reached not reachable code");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}