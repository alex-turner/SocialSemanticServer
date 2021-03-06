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
package at.kc.tugraz.ss.service.tag.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserGetPar;
import at.kc.tugraz.ss.service.tag.impl.fct.sql.SSTagSQLFct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSTagMiscFct {

  public static List<SSUri> getEntitiesForTagsIfSpaceNotSet(
    final SSTagSQLFct                    sqlFct,
    final SSTagUserEntitiesForTagsGetPar par) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    
    if(par.labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntitiesForTagLabel(
          par.user,
          null,
          SSSpaceE.privateSpace));
      
      entities.addAll(
        sqlFct.getEntitiesForTagLabel(
          par.forUser,
          null,
          SSSpaceE.sharedSpace));
    }
    
    //TODO dtheiler: handle loop in db
    for(SSTagLabel label : par.labels){
      
      entities.addAll(
        sqlFct.getEntitiesForTagLabel(
          par.user,
          label,
          SSSpaceE.privateSpace));
      
      entities.addAll(
        sqlFct.getEntitiesForTagLabel(
          par.forUser,
          label,
          SSSpaceE.sharedSpace));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSUri> getEntitiesForTagsIfSpaceSet(
    final SSTagSQLFct                    sqlFct,
    final SSTagUserEntitiesForTagsGetPar par,
    final SSUri                          userToUse) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    
    if(par.labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntitiesForTagLabel(
          userToUse,
          null,
          par.space));
    }
    
    //TODO dtheiler: handle loop in db
    for(SSTagLabel label : par.labels){
      
      entities.addAll(
        sqlFct.getEntitiesForTagLabel(
          userToUse,
          label,
          par.space));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSTag> getTagsIfSpaceNotSet(
    final SSTagSQLFct      sqlFct,
    final SSTagsUserGetPar par) throws Exception{
    
    final List<SSTag> tags = new ArrayList<>();
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        tags.addAll (sqlFct.getTagAsss(par.forUser, null, null, SSSpaceE.sharedSpace,  par.startTime));
        tags.addAll (sqlFct.getTagAsss(par.user,    null, null, SSSpaceE.privateSpace, par.startTime));
      }
      
      for(SSTagLabel label : par.labels){
        tags.addAll (sqlFct.getTagAsss(par.forUser, null, label, SSSpaceE.sharedSpace,  par.startTime));
        tags.addAll (sqlFct.getTagAsss(par.user,    null, label, SSSpaceE.privateSpace, par.startTime));
      }
    }
    
    //TODO dtheiler: handle loops in db
    for(SSUri entity : par.entities){
      
      if(par.labels.isEmpty()){
        tags.addAll (sqlFct.getTagAsss(par.forUser, entity, null, SSSpaceE.sharedSpace,  par.startTime));
        tags.addAll (sqlFct.getTagAsss(par.user,    entity, null, SSSpaceE.privateSpace, par.startTime));
      }
      
      for(SSTagLabel label : par.labels){
        tags.addAll (sqlFct.getTagAsss(par.forUser,     entity, label, SSSpaceE.sharedSpace,  par.startTime));
        tags.addAll (sqlFct.getTagAsss(par.user,        entity, label, SSSpaceE.privateSpace, par.startTime));
      }
    }
    
    return tags;
  }
  
  public static List<SSTag> getTagsIfSpaceSet(
    final SSTagSQLFct      sqlFct, 
    final SSTagsUserGetPar par,
    final SSUri            userToUse) throws Exception{
    
    final List<SSTag> tags = new ArrayList<>();
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        tags.addAll (sqlFct.getTagAsss(userToUse, null, null, par.space, par.startTime));
      }
      
      for(SSTagLabel label : par.labels){
        tags.addAll (sqlFct.getTagAsss(userToUse, null, label, par.space, par.startTime));
      }
    }
    
    //TODO dtheiler: handle loops in db
    for(SSUri entity : par.entities){
      
      if(par.labels.isEmpty()){
        tags.addAll (sqlFct.getTagAsss(userToUse, entity, null, par.space, par.startTime));
      }
      
      for(SSTagLabel label : par.labels){
        tags.addAll (sqlFct.getTagAsss(userToUse, entity, label, par.space, par.startTime));
      }
    }
    
    return tags;
  }
  
  public static List<SSTagFrequ> getTagFrequsFromTags(
    final List<SSTag> tags,
    final SSSpaceE    space) throws Exception{
    
    final Map<String, Integer> counterPerTags = new HashMap<>();
    
    String tagLabel;
    
    for (SSTag tag : tags) {
      
      tagLabel = SSStrU.toStr(tag.label);
      
      if(counterPerTags.containsKey(tagLabel)){
        counterPerTags.put(tagLabel, counterPerTags.get(tagLabel) + 1);
      } else {
        counterPerTags.put(tagLabel, 1);
      }
    }
    
    final List<SSTagFrequ> outList = new ArrayList<>(counterPerTags.size());
    
    for(String key : counterPerTags.keySet()){
      
      outList.add(
        SSTagFrequ.get(
          SSTagLabel.get(key),
          space,
          counterPerTags.get(key)));
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