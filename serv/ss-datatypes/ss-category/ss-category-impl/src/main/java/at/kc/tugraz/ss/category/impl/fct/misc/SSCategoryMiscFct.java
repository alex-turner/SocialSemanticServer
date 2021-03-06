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
package at.kc.tugraz.ss.category.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesUserGetPar;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryFrequ;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserEntitiesForCategoriesGetPar;
import at.kc.tugraz.ss.category.impl.fct.sql.SSCategorySQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCategoryMiscFct {

  public static List<SSUri> getEntitiesForCategoriesIfSpaceNotSet(
    final SSCategorySQLFct                          sqlFct,
    final SSCategoryUserEntitiesForCategoriesGetPar par) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    
    if(par.labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntitiesForCategoryLabel(
          par.user,
          null,
          SSSpaceE.privateSpace));
      
      entities.addAll(
        sqlFct.getEntitiesForCategoryLabel(
          par.forUser,
          null,
          SSSpaceE.sharedSpace));
    }
    
    //TODO dtheiler: handle loop in db
    for(SSCategoryLabel label : par.labels){
      
      entities.addAll(
        sqlFct.getEntitiesForCategoryLabel(
          par.user,
          label,
          SSSpaceE.privateSpace));
      
      entities.addAll(
        sqlFct.getEntitiesForCategoryLabel(
          par.forUser,
          label,
          SSSpaceE.sharedSpace));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSUri> getEntitiesForCategoriesIfSpaceSet(
    final SSCategorySQLFct                          sqlFct,
    final SSCategoryUserEntitiesForCategoriesGetPar par,
    final SSUri                                     userToUse) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    
    if(par.labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntitiesForCategoryLabel(
          userToUse,
          null,
          par.space));
    }
    
    //TODO dtheiler: handle loop in db
    for(SSCategoryLabel label : par.labels){
      
      entities.addAll(
        sqlFct.getEntitiesForCategoryLabel(
          userToUse,
          label,
          par.space));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSCategory> getCategoriesIfSpaceNotSet(
    final SSCategorySQLFct       sqlFct,
    final SSCategoriesUserGetPar par) throws Exception{
    
    final List<SSCategory> categories = new ArrayList<>();
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(par.forUser, null, null, SSSpaceE.sharedSpace,  par.startTime));
        categories.addAll (sqlFct.getCategoryAsss(par.user,    null, null, SSSpaceE.privateSpace, par.startTime));
      }
      
      for(SSCategoryLabel label : par.labels){
        categories.addAll (sqlFct.getCategoryAsss(par.forUser, null, label, SSSpaceE.sharedSpace,  par.startTime));
        categories.addAll (sqlFct.getCategoryAsss(par.user,    null, label, SSSpaceE.privateSpace, par.startTime));
      }
    }
    
    //TODO dtheiler: handle loops in db
    for(SSUri entity : par.entities){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(par.forUser, entity, null, SSSpaceE.sharedSpace,  par.startTime));
        categories.addAll (sqlFct.getCategoryAsss(par.user,    entity, null, SSSpaceE.privateSpace, par.startTime));
      }
      
      for(SSCategoryLabel label : par.labels){
        categories.addAll (sqlFct.getCategoryAsss(par.forUser,     entity, label, SSSpaceE.sharedSpace,  par.startTime));
        categories.addAll (sqlFct.getCategoryAsss(par.user,        entity, label, SSSpaceE.privateSpace, par.startTime));
      }
    }
    
    return categories;
  }
  
  public static List<SSCategory> getCategoriesIfSpaceSet(
    final SSCategorySQLFct       sqlFct, 
    final SSCategoriesUserGetPar par,
    final SSUri                  userToUse) throws Exception{
    
    final List<SSCategory> categories = new ArrayList<>();
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(userToUse, null, null, par.space, par.startTime));
      }
      
      for(SSCategoryLabel label : par.labels){
        categories.addAll (sqlFct.getCategoryAsss(userToUse, null, label, par.space, par.startTime));
      }
    }
    
    //TODO dtheiler: handle loops in db
    for(SSUri entity : par.entities){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(userToUse, entity, null, par.space, par.startTime));
      }
      
      for(SSCategoryLabel label : par.labels){
        categories.addAll (sqlFct.getCategoryAsss(userToUse, entity, label, par.space, par.startTime));
      }
    }
    
    return categories;
  }
  
  public static List<SSCategoryFrequ> getCategoryFrequsFromCategories(
    final List<SSCategory> categories,
    final SSSpaceE         space) throws Exception{
    
    final Map<String, Integer> counterPerCategories = new HashMap<>();
    
    String categoryLabel;
    
    for (SSCategory category : categories) {
      
      categoryLabel = SSStrU.toStr(category.label);
      
      if(counterPerCategories.containsKey(categoryLabel)){
        counterPerCategories.put(categoryLabel, counterPerCategories.get(categoryLabel) + 1);
      } else {
        counterPerCategories.put(categoryLabel, 1);
      }
    }
    
    final List<SSCategoryFrequ> outList = new ArrayList<>(counterPerCategories.size());
    
    for(String key : counterPerCategories.keySet()){
      
      outList.add(
        SSCategoryFrequ.get(
          SSCategoryLabel.get(key),
          space,
          counterPerCategories.get(key)));
    }
    
    return outList;
  }
}

//  private void saveUETagAdd(SSServPar parA) throws Exception {
//    
//    Map<String, Object> opPars = new HashMap<>();
//    SSTagAddPar par = new SSTagAddPar(parA);
//    
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.eventType,    SSUEEnum.useTag);
//    opPars.put(SSVarU.content,      SSStrU.toStr(par.tagString));
//    
//    SSServReg.callServServer(new SSServPar(SSMethU.uEAdd, opPars));
//    
//    opPars = new HashMap<>();
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.content,      SSStrU.toStr(par.tagString));
//    
//    if(SSSpaceEnum.isShared(par.space)) {
//      opPars.put(SSVarU.eventType,    SSUEEnum.addSharedTag);
//    } else {
//      opPars.put(SSVarU.eventType,    SSUEEnum.addPrivateTag);
//    }
//    
//    SSServReg.callServServer(new SSServPar(SSMethU.uEAdd, opPars));
//  }
  
//  private void saveUETagDelete(SSServPar parA) throws Exception{
//    
//    Map<String, Object> opPars = new HashMap<>();
//    SSTagAssRemovePar par = new SSTagAssRemovePar(parA);
//    
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.content,      SSStrU.toStr(par.tagString));
//    
//    if (SSSpaceEnum.isShared(par.space)) {
//      opPars.put(SSVarU.eventType,    SSUEEnum.removeSharedTag);
//    } else {
//      opPars.put(SSVarU.eventType,    SSUEEnum.removePrivateTag);
//    }
//    
//    SSServReg.callServServer(new SSServPar(SSMethU.uEAdd, opPars));
//  }
//  public static String[] getStringArrayFromList(
//          List    list,
//          boolean deleteNamespace)  {
//
//    String[] outString = new String[list.size()];
//    int      i         = 0;
//    String   namespace = Vocabulary.getInstance().getNamespace(VocNamespace.EMPTY, false).toString();
//    Iterator iterator  = list.iterator();
//    String   tagString = null;
//    while(iterator.hasNext()) {
//      
//      tagString = (String) iterator.next();
//      
//      if (deleteNamespace) {
////        tagString = tagString.replaceAll("http://tug.mature-ip.eu/", strU.strEmpty);
//        tagString = tagString.replaceAll(namespace, strU.strEmpty);
//      }
//
//      outString[i] = tagString;
//
//      System.out.print(outString[i] + "|");
//
//      i++;
//    }
//
//    return outString;
//  }

//  public static Map<String, Long> getCreationTimePerTagFromTags(final List<SSTag> tags) throws Exception{
//    
//    final Map<String, Long> creationTimesPerTag = new HashMap<>();
//    
//    for(SSTag tag : tags){
//      creationTimesPerTag.put(tag.label.toString(), entityCreationTimeGet(tag.uri));
//    }
//    
//    return creationTimesPerTag;
//  }