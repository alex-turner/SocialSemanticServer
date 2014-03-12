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
package at.kc.tugraz.ss.service.coll.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCollSQLFct extends SSDBSQLFct{

  public SSCollSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public SSUri createColl(final SSUri collUri) throws Exception{
    
     if(collUri == null){
      SSServErrReg.regErrThrow(new Exception("colluri null"));
      return null;
    }
     
    final Map<String, String> insertPars = new HashMap<String, String>();
    
    insertPars.put(SSSQLVarU.collId, collUri.toString());
    
    dbSQL.insert(collTable, insertPars);
    
    return collUri;
  }
  
  public void removeColl(final SSUri collUri) throws Exception{
    
    if(collUri == null){
      SSServErrReg.regErrThrow(new Exception("colluri null"));
      return;
    }
    
    final List<String> subCollUris = new ArrayList<String>();
    Map<String, String> deletePars;
    
    try{
      
      //retrieve all sub coll uris
      getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
          
      //remove all sub colls
      for(String subCollUri : subCollUris){
        
        deletePars = new HashMap<String, String>();
        deletePars.put(SSSQLVarU.collId, subCollUri);
        
        dbSQL.deleteWhere(collTable, deletePars);
        
        deletePars = new HashMap<String, String>();
        deletePars.put(SSSQLVarU.entryId, subCollUri);
        
        dbSQL.deleteWhere(collEntryPosTable, deletePars);
      }
      
      deletePars = new HashMap<String, String>();
      deletePars.put(SSSQLVarU.collId, SSUri.toStr(collUri));

      dbSQL.deleteWhere(collTable, deletePars);
      
      deletePars = new HashMap<String, String>();
      deletePars.put(SSSQLVarU.entryId, SSUri.toStr(collUri));

      dbSQL.deleteWhere(collEntryPosTable, deletePars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addUserRootColl(final SSUri rootCollUri, final SSUri userUri) throws Exception{
    
    if(SSObjU.isNull(rootCollUri, userUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return;
    }
    
    Map<String, String> insertPars;
        
    //add coll to coll root table
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.userId, userUri.toString());
    insertPars.put(SSSQLVarU.collId, rootCollUri.toString());
    
    dbSQL.insert(collRootTable, insertPars);
    
    //add coll to user coll table
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.userId,    userUri.toString());
    insertPars.put(SSSQLVarU.collId,    rootCollUri.toString());
    insertPars.put(SSSQLVarU.collSpace, SSSpaceEnum.privateSpace.toString());
    
    dbSQL.insert(collUserTable, insertPars);
  }
  
  public Boolean isRootColl(final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(entityUri)){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
        
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.collId, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collRootTable, whereParNamesWithValues);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean isColl(final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(entityUri)){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.collId, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collTable, whereParNamesWithValues);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addEntryToColl(
    final SSUri      collParent, 
    final SSUri      collEntry, 
    final SSLabelStr entryLabel) throws Exception{
    
    if(SSObjU.isNull(collParent, collEntry, entryLabel)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return;
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>();
    
    //add coll entry to coll entry pos table
    Integer collEntryCount = getCollEntryCount(collParent);
    
    collEntryCount++;
    
    insertPars.put(SSSQLVarU.collId,  collParent.toString());
    insertPars.put(SSSQLVarU.entryId, collEntry.toString());
    insertPars.put(SSSQLVarU.pos,     collEntryCount.toString());
    
    dbSQL.insert(collEntryPosTable, insertPars);
  }

  public Integer getCollEntryCount(SSUri coll) throws Exception{
    
    Map<String, String> selectPars     = new HashMap<String, String>();
    ResultSet           resultSet      = null;
    Integer             collEntryCount = 0;

    selectPars.put(SSSQLVarU.collId, coll.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
      
      resultSet.last();
      
      collEntryCount = new Integer(resultSet.getRow());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return collEntryCount;
  }
  
  public List<String> getAllUserCollURIs(final SSUri userUri) throws Exception{
     
    final Map<String, String> whereParNamesWithValues   = new HashMap<String, String>();
    final List<String>        userCollUris              = new ArrayList<String>();
    ResultSet                 resultSet                 = null;
    
    whereParNamesWithValues.put(SSSQLVarU.userId, userUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        userCollUris.add(bindingStr(resultSet, SSSQLVarU.collId));
      }
      
      return userCollUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  public Boolean followsUserColl(final SSUri userUri, final SSUri collUri) throws Exception{
    
    final Map<String, String> whereParNamesWithValues  = new HashMap<String, String>();
    ResultSet                 resultSet                = null;
    
    whereParNamesWithValues.put(SSSQLVarU.userId,    userUri.toString());
    whereParNamesWithValues.put(SSSQLVarU.collId,    collUri.toString());
    whereParNamesWithValues.put(SSSQLVarU.collSpace, SSSpaceEnum.followSpace.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean followsUserAParentOrSubColl(final SSUri userUri, final SSUri collUri) throws Exception{
    
    final List<String> subCollUris    = new ArrayList<String>();
    final List<String> parentCollUris = new ArrayList<String>();
    
    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
    
    for(String subCollUri : subCollUris){
      
      if(ownsUserColl(userUri, SSUri.get(subCollUri))){
        return true;
      }
    }
    
    getAllParentCollURIs(collUri.toString(), collUri.toString(), parentCollUris);
    
    for(String parentCollUri : parentCollUris){
      
      if(ownsUserColl(userUri, SSUri.get(parentCollUri))){
        return true;
      }
    }
    
    return false;
  }
  
  public void addCollToUserColl(
    final SSUri       user, 
    final SSUri       collParent, 
    final SSUri       collChild, 
    final SSSpaceEnum collChildSpace, 
    final SSLabelStr  collChildLabel) throws Exception{
    
    Map<String, String> insertPars;
    
    //add relation of coll parent to child coll to hierarchy table
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.collParentId, collParent.toString());
    insertPars.put(SSSQLVarU.collChildId,  collChild.toString());
    
    dbSQL.insert(collHierarchyTable, insertPars);
    
    //add coll child to coll parent in coll entry pos table
    Integer collEntryCount = getCollEntryCount(collParent);
    
    collEntryCount++;
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.collId,  collParent.toString());
    insertPars.put(SSSQLVarU.entryId, collChild.toString());
    insertPars.put(SSSQLVarU.pos,     collEntryCount.toString());
    
    dbSQL.insert(collEntryPosTable, insertPars);
    
    //add child coll to user coll table
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.userId,    user.toString());
    insertPars.put(SSSQLVarU.collId,    collChild.toString());
    insertPars.put(SSSQLVarU.collSpace, collChildSpace.toString());
    
    dbSQL.insert(collUserTable, insertPars);
    
    //add currently added collection to other users as well
    if(SSSpaceEnum.isShared(collChildSpace)){

      insertPars = new HashMap<String, String>();
      insertPars.put(SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
      insertPars.put(SSSQLVarU.collId,    collChild.toString());
        
      for(SSUri coUserUri : getCollUsers(collParent)){
        
        if(SSStrU.equals(coUserUri.toString(), user.toString())){
          continue;
        }
        
        insertPars.put(SSSQLVarU.userId,    coUserUri.toString());
        
        dbSQL.insert(collUserTable, insertPars);
      }
    }
    
    //add sub colls of to to be followed coll for this user as well
    if(SSSpaceEnum.isFollow(collChildSpace)){
      
      final List<String> subCollUris = new ArrayList<String>();
      
      getAllChildCollURIs(collChild.toString(), collChild.toString(), subCollUris);
      
      insertPars = new HashMap<String, String>();
      insertPars.put(SSSQLVarU.userId,    user.toString());
      insertPars.put(SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
      
      for(String subCollUri : subCollUris){
        
        insertPars.put(SSSQLVarU.collId, subCollUri);
        
        dbSQL.insert(collUserTable, insertPars);
      }
    }
  }

  public void unfollowColl(final SSUri user, final SSUri parentColl, final SSUri coll) throws Exception{
    
    Map<String, String> deletePars;

    //remove sub colls of followed coll from user coll table as well
    final List<String> subCollUris = new ArrayList<String>();
    
    getAllChildCollURIs(coll.toString(), coll.toString(), subCollUris);
    
    deletePars = new HashMap<String, String>();
    deletePars.put(SSSQLVarU.userId, user.toString());
    
    for(String subCollUri : subCollUris){
      
      deletePars.put(SSSQLVarU.collId, subCollUri);
      
      dbSQL.deleteWhere(collUserTable, deletePars);
    }
    
    //remove coll from user coll table
    deletePars = new HashMap<String, String>();
    deletePars.put(SSSQLVarU.userId,     user.toString());
    deletePars.put(SSSQLVarU.collId,     coll.toString());
            
    dbSQL.deleteWhere(collUserTable, deletePars);
    
    //remove coll from coll hierarchy table
    deletePars = new HashMap<String, String>();
    deletePars.put(SSSQLVarU.collParentId, parentColl.toString());
    deletePars.put(SSSQLVarU.collChildId,  coll.toString());
            
    dbSQL.deleteWhere(collHierarchyTable, deletePars);
    
    //remove coll from coll entries pos table
    deletePars = new HashMap<String, String>();
    deletePars.put(SSSQLVarU.collId,   parentColl.toString());
    deletePars.put(SSSQLVarU.entryId,  coll.toString());
            
    dbSQL.deleteWhere(collEntryPosTable, deletePars);
  }

  public void getAllChildCollURIs(
    final String       startCollUri, 
    final String       currentCollUri, 
    final List<String> subCollUris) throws Exception{
    
    for(String directSubCollUri : getDirectChildCollURIs(currentCollUri)){
      getAllChildCollURIs(startCollUri, directSubCollUri, subCollUris);
    }
    
    if(startCollUri.equals(currentCollUri)){
      return;
    }
    
    if(!subCollUris.contains(currentCollUri)){
      subCollUris.add(currentCollUri);
    }
  }
  
  public void getAllParentCollURIs(
    final String       startCollUri, 
    final String       currentCollUri, 
    final List<String> parentCollUris) throws Exception{
    
    for(String directParentCollUri : getDirectParentCollURIs(currentCollUri)){
      getAllParentCollURIs(startCollUri, directParentCollUri, parentCollUris);
    }
    
    if(startCollUri.equals(currentCollUri)){
      return;
    }
    
    if(!parentCollUris.contains(currentCollUri)){
      parentCollUris.add(currentCollUri);
    }
  }
  
  private List<String> getDirectChildCollURIs(final String collUri) throws Exception{
    
    final Map<String, String> whereParNamesWithValues   = new HashMap<String, String>();
    final List<String>        directSubCollUris         = new ArrayList<String>();
    ResultSet                 resultSet                 = null;
    
    whereParNamesWithValues.put(SSSQLVarU.collParentId, collUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collHierarchyTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        directSubCollUris.add(resultSet.getString(SSSQLVarU.collChildId));
      }
      
      return directSubCollUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private List<String> getDirectParentCollURIs(final String collUri) throws Exception{
    
    final List<String>        directParentCollUris    = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    try{
      whereParNamesWithValues.put(SSSQLVarU.collChildId, collUri);
      
      resultSet = dbSQL.selectAllWhere(collHierarchyTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        directParentCollUris.add(bindingStr(resultSet, SSSQLVarU.collParentId));
      }
      
      return directParentCollUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeEntryFromColl(SSUri coll, SSUri collEntry) throws Exception{

    Map<String, String> deletePars;
    
    //remove coll entry from coll entry pos table
    deletePars = new HashMap<String, String>();
    deletePars.put(SSSQLVarU.collId,  coll.toString());
    deletePars.put(SSSQLVarU.entryId, collEntry.toString());
      
    dbSQL.deleteWhere(collEntryPosTable, deletePars);
  }

  public List<SSUri> getAllSharedCollURIs() throws Exception{
    
    final List<SSUri>         sharedCollURIs          = new ArrayList<SSUri>();
    final List<String>        columnNames             = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    //get all colls from user table where space is shared (distinct)
    columnNames.add             (SSSQLVarU.collId);
    whereParNamesWithValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
    
    try{
      resultSet = dbSQL.selectCertainDistinctWhere(collUserTable, columnNames, whereParNamesWithValues);

      while(resultSet.next()){
        sharedCollURIs.add(bindingStrToUri(resultSet, SSSQLVarU.collId));
      }
      
      return sharedCollURIs;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void shareColl(SSUri coll) throws Exception{
    
    Map<String, String> updatePars;
    Map<String, String> newValues;
    
    //set space in user coll table for coll to shared
    newValues  = new HashMap<String, String>();
    updatePars = new HashMap<String, String>();
    updatePars.put(SSSQLVarU.collId,    coll.toString());
    newValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
    
    dbSQL.updateWhere(collUserTable, updatePars, newValues);
  }

  public void changeCollEntriesPos(final SSUri coll, final List<SSUri> collEntries, final List<Integer> order) throws Exception{
  
    if(
      SSObjU.isNull(collEntries, order) ||
      collEntries.size() != order.size()){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return;
    }
    
    final Map<String, String> updatePars = new HashMap<String, String>();
    final Map<String, String> newValues  = new HashMap<String, String>();
    Integer                   counter    = 0;
    
    updatePars.put(SSSQLVarU.collId,  coll.toString());
    
    while(counter < collEntries.size()){
      
      updatePars.put(SSSQLVarU.entryId, collEntries.get(counter).toString());
      newValues.put (SSSQLVarU.pos,     order.get      (counter).toString());
      
      counter++;
      
      dbSQL.updateWhere(collEntryPosTable, updatePars, newValues);
    }
  }
  
  public SSColl getColl(final SSUri collUri, final SSSpaceEnum collSpace) throws Exception{
   
    if(SSObjU.isNull(collUri, collSpace)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    final List<String>        tableNames              = new ArrayList<String>();
    final List<String>        columnNames             = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    SSColl                    coll                    = null;
    ResultSet                 resultSet               = null;
    
    try{
      tableNames.add              (collTable);
      tableNames.add              (entityTable);
      columnNames.add             (SSSQLVarU.collId);
      columnNames.add             (SSSQLVarU.author);
      columnNames.add             (SSSQLVarU.label);
      whereParNamesWithValues.put (SSSQLVarU.collId, collUri.toString());
      
      resultSet =
        dbSQL.selectCertainWhere(
        tableNames,
        columnNames,
        whereParNamesWithValues,
        SSSQLVarU.collId + SSStrU.equal + SSSQLVarU.id);
      
      resultSet.first();
      
      coll = 
        SSColl.get(
        collUri, 
        null, 
        bindingStrToUri  (resultSet, SSSQLVarU.author), 
        bindingStr       (resultSet, SSSQLVarU.label), 
        collSpace);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return coll;
  }
  
  public SSColl getUserColl(final SSUri userUri, final SSUri collUri) throws Exception{
   
    if(SSObjU.isNull(userUri, collUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    if(!ownsUserColl(userUri, collUri)){
      SSServErrReg.regErrThrow(new Exception("user doesnt have access to coll"));
      return null;
    }
    
    final List<String>        tableNames              = new ArrayList<String>();
    final List<String>        columnNames             = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    try{
      tableNames.add              (collUserTable);
      tableNames.add              (entityTable);
      columnNames.add             (SSSQLVarU.userId);
      columnNames.add             (SSSQLVarU.collId);
      columnNames.add             (SSSQLVarU.collSpace);
      columnNames.add             (SSSQLVarU.author);
      columnNames.add             (SSSQLVarU.label);
      whereParNamesWithValues.put (SSSQLVarU.userId, userUri.toString());
      whereParNamesWithValues.put (SSSQLVarU.collId, collUri.toString());
      
      resultSet =
        dbSQL.selectCertainWhere(
        tableNames,
        columnNames,
        whereParNamesWithValues,
        SSSQLVarU.collId + SSStrU.equal + SSSQLVarU.id);
      
      resultSet.first();
      
      return SSColl.get(
        collUri,
        null,
        bindingStrToUri  (resultSet, SSSQLVarU.author),
        bindingStr       (resultSet, SSSQLVarU.label),
        bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  
  //    IF(OWNSUSERCOLLBYHIERARCHY(USERURI, COLLURI)){
//      
//      TRY{
//        
//        WHEREPARNAMESWITHVALUES.PUT(SSSQLVARU.ID, COLLURI.TOSTRING());
//        
//        RESULTSET = DBSQL.DBSQLSELECTALLWHERE(ENTITYTABLE, WHEREPARNAMESWITHVALUES);
//        
//        RESULTSET.FIRST();
//        
//        RETURN SSCOLL.GET(
//          COLLURI,
//          NULL,
//          BINDINGSTRTOURI  (RESULTSET, SSSQLVARU.AUTHOR),
//          BINDINGSTR       (RESULTSET, SSSQLVARU.LABEL),
//          SSSPACEENUM.SHAREDSPACE);
//        
//      }CATCH(EXCEPTION ERROR){
//        SSSERVERRREG.REGERRTHROW(ERROR);
//        RETURN NULL;
//      }FINALLY{
//        DBSQL.DBSQLCLOSESTMT(RESULTSET);
//      }
//    }
//    
//    SSSERVERRREG.REGERRTHROW(NEW EXCEPTION("COLL NEITHER IS OWNED NOR FOLLOWED BY USER"));
//    RETURN NULL;
  }
  
  public SSColl getUserCollWithEntries(
    final SSUri   userUri, 
    final SSUri   collUri,
    final Boolean sort) throws Exception{
    
    if(SSObjU.isNull(userUri, collUri, sort)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
        
    final List<String>        tableNames              = new ArrayList<String>();
    final List<String>        columnNames             = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    final SSColl              coll;
    ResultSet                 resultSet               = null;
    SSCollEntry               collEntry;
    
    try{
      
      coll = getUserColl(userUri, collUri);
      
      tableNames.add              (collEntryPosTable); 
      tableNames.add              (entityTable);
      columnNames.add             (SSSQLVarU.entryId);
      columnNames.add             (SSSQLVarU.pos);
      columnNames.add             (SSSQLVarU.label);
      columnNames.add             (SSSQLVarU.type);
      
      whereParNamesWithValues.put (SSSQLVarU.collId, coll.uri.toString());
        
      resultSet = 
        dbSQL.selectCertainWhereOrderBy(
        tableNames, 
        columnNames, 
        whereParNamesWithValues, 
        SSSQLVarU.entryId + SSStrU.equal + SSSQLVarU.id,
        SSSQLVarU.pos,
        "ASC");
      
      while(resultSet.next()){
        
        collEntry =
          SSCollEntry.get(
          bindingStrToUri (resultSet, SSSQLVarU.entryId),
          bindingStr             (resultSet, SSSQLVarU.label),
          coll.space,
          bindingStrToInteger    (resultSet, SSSQLVarU.pos),
          bindingStrToEntityType (resultSet, SSSQLVarU.type));
        
        //coll entry is a coll itself
        if(isColl(collEntry.uri)){
          collEntry.space = getUserColl(userUri, collEntry.uri).space;
        }
        
        coll.entries.add(collEntry);
      }
      
      return coll;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSUri getUserRootCollURI(SSUri user) throws Exception{
    
    final Map<String, String> selectPars      = new HashMap<String, String>();
    ResultSet                 resultSet       = null;
    
    selectPars.put(SSSQLVarU.userId, user.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collRootTable, selectPars);

      resultSet.first();

      return bindingStrToUri(resultSet, SSSQLVarU.collId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSUri getUserDirectParentCollURI(final SSUri userUri, final SSUri childColl) throws Exception{
    
    if(isRootColl(childColl)){
      return childColl;
    }
    
    for(String parentCollUri : getDirectParentCollURIs(childColl.toString())){ 
      
      if(ownsUserColl(userUri, SSUri.get(parentCollUri))){
        return SSUri.get(parentCollUri);
      }
    }
    
    SSServErrReg.regErrThrow(new Exception("user doesnt own coll"));
    return null;
  }

  public Boolean containsEntry(SSUri collUri, SSUri collEntryUri) throws Exception{
    
    if(SSObjU.isNull(collUri, collEntryUri)){
      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
      return null;
    }
    
    ResultSet           resultSet               = null;
    Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    
    whereParNamesWithValues.put(SSSQLVarU.collId,  SSUri.toStr(collUri));
    whereParNamesWithValues.put(SSSQLVarU.entryId, SSUri.toStr(collEntryUri));
    
    try{
      resultSet = dbSQL.selectAllWhere(collEntryPosTable, whereParNamesWithValues);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean existsUserRootColl(SSUri user) throws Exception{
    
    final Map<String, String> selectPars         = new HashMap<String, String>();
    ResultSet                 resultSet          = null;
    
    selectPars.put(SSSQLVarU.userId, user.toString());
    
    try{
      resultSet  = dbSQL.selectAllWhere(collRootTable, selectPars);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri createCollURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objColl().toString()));
  }
  
  private SSUri objColl() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.coll.toString());
  }  
  
  private Boolean ownsUserColl(final SSUri userUri, final SSUri collUri) throws Exception {
    
    final Map<String, String> whereParNamesWithValues  = new HashMap<String, String>();
    ResultSet                 resultSet                = null;
    
    whereParNamesWithValues.put(SSSQLVarU.userId, userUri.toString());
    whereParNamesWithValues.put(SSSQLVarU.collId, collUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);

      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  private List<SSUri> getCollUsers(SSUri collUri) throws Exception{
    
    final Map<String, String> whereParNamesWithValues  = new HashMap<String, String>();
    final List<SSUri>         users                    = new ArrayList<SSUri>();
    ResultSet                 resultSet                = null;
    
    whereParNamesWithValues.put(SSSQLVarU.collId, collUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        users.add(bindingStrToUri(resultSet, SSSQLVarU.userId));
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<String> getCollUrisContainingEntity(final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(entityUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
        
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    final List<String>        collUris                = new ArrayList<String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.entryId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(collEntryPosTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        collUris.add(bindingStr(resultSet, SSSQLVarU.collId));
      }
      
      return collUris;
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}

//  public SSSpaceEnum getUserCollSpace(SSUri user, SSUri coll) throws Exception{
//    
//    Map<String, String> selectPars    = new HashMap<String, String>();
//    ResultSet           resultSet     = null;
//    SSSpaceEnum         userCollSpace = null;
//    
//    selectPars.put(SSSQLVarU.userId, user.toString());
//    selectPars.put(SSSQLVarU.collId, coll.toString());
//    
//    try{
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      resultSet.first();
//      
//      userCollSpace = SSSpaceEnum.get(resultSet.getString(SSSQLVarU.collSpace));
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return userCollSpace;
//  }

//private Boolean ownsUserCollByHierarchy(SSUri userUri, SSUri collUri) throws Exception{
//    
//    final List<String> collParents    = new ArrayList<String>();
//    final List<String> newCollParents = new ArrayList<String>();
//    
//    try{
//      
//      collParents.addAll(getCollDirectParentURIs(collUri));
//      
//      while(true){
//        
//        for(String parentCollUri : collParents){
//          
//          for(String tmpUri : getCollDirectParentURIs(SSUri.get(parentCollUri))){
//          
//            if(!newCollParents.contains(tmpUri)){
//              newCollParents.add(tmpUri);
//            }
//          }
//        }
//        
//        if(newCollParents.isEmpty()){
//          return false;
//        }
//        
//        for(String newCollParentUri : newCollParents){
//          
//          if(ownsUserColl(userUri, SSUri.get(newCollParentUri))){
//            return true;
//          }
//        }
//        
//        collParents.clear();
//        collParents.addAll(newCollParents);
//        newCollParents.clear();
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public SSSpaceEnum getCollSpace(SSUri userUri, SSUri collUri) throws Exception{
//    
//    if(
//      userUri      == null ||
//      collUri      == null){
//      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
//      return null;
//    }
//        
//    Map<String, String> selectPars  = new HashMap<String, String>();
//    ResultSet           resultSet   = null;
//    SSSpaceEnum         collSpace   = null;
//    
//    selectPars.put(SSSQLVarU.userId, userUri.toString());
//    selectPars.put(SSSQLVarU.collId, collUri.toString());
//    
//    try{
//      
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      if(!resultSet.first()){
//        SSServErrReg.regErrThrow(new Exception("coll-user combination does not exist"));
//      }
//
//      collSpace = bindingStrToSpace(resultSet, SSSQLVarU.collSpace);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return collSpace;
//  }

// public Boolean isEntityInPrivateUserColl(SSUri user, SSUri entity) throws Exception{
//
//    Map<String, String> selectPars                = new HashMap<String, String>();
//    ResultSet           resultSet                 = null;
//    List<String>        parentCollUris            = new ArrayList<String>();
//    Boolean             isEntityInPrivateUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInPrivateUserColl = SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInPrivateUserColl;
//        }else{
//          return false;
//        }
//        
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<String, String>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<String, String>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//public Boolean isEntityInSharedOrFollowedUserColl(final SSUri user, SSUri entity) throws Exception{
//
//    Map<String, String> selectPars;
//    ResultSet           resultSet                            = null;
//    List<String>        parentCollUris                       = new ArrayList<String>();
//    Boolean             isEntityInSharedOrFollowedUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars = new HashMap<String, String>();
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInSharedOrFollowedUserColl = SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInSharedOrFollowedUserColl;
//        }else{
//          return false;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<String, String>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<String, String>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//  public Boolean newIsEntityInPrivateUserColl(final SSUri userUri, final SSUri entityUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    SSColl                    coll;
//      
//    try{
//      collUris = getCollUrisContainingEntity(entityUri);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//    
//    if(collUris.isEmpty()){
//      return false;
//    }
//    
//    for(String collUri : collUris){
//      
//      try{
//        coll = getUserColl(userUri, SSUri.get(collUri));
//      }catch(Exception error){
//        continue;
//      }
//      
//      if(SSSpaceEnum.isPrivate(coll.space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
//  public Boolean newIsEntityInSharedOrFollowedUserColl(final SSUri userUri, final SSUri entityUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    final List<String>        userCollUris;
//    SSColl                    coll;
//    
//    try{
//      userCollUris  = getAllUserCollURIs(userUri);
//      
//      if(userCollUris.isEmpty()){
//        return false;
//      }
//      
//      collUris      = getCollUrisContainingEntity(entityUri);
//      
//      if(collUris.isEmpty()){
//        return false;
//      }
//      
//      for(String userCollUri : userCollUris){
//        
//        if(!collUris.contains(userCollUri)){
//          continue;
//        }
//        
//        coll = getUserColl(userUri, SSUri.get(userCollUri));
//        
//        if(SSSpaceEnum.isSharedOrFollow(coll.space)){
//          return true;
//        }
//      }
//      
//      return false;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }