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
package at.kc.tugraz.ss.datatypes.datatypes.entity;

import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SSEntityDescA extends SSEntityA{

	public SSLabel          label           = null;
	public Long             creationTime    = null;
  public SSUri            entityUri       = null;
  public SSEntityE        entityType      = null;
  public SSEntityE        entityDescType  = null;
  public SSUri            author          = null;
  public SSEntityA        overallRating   = null;
  public List<String>     tags            = new ArrayList<String>();
  public List<SSUri>      discs           = new ArrayList<SSUri>();
	
  protected SSEntityDescA(
    final SSUri        entityUri, 
    final SSLabel      label, 
    final Long         creationTime, 
    final SSEntityE    entityType, 
    final SSEntityE    entityDescType,
    final SSUri        author,
    final SSEntityA    overallRating,
    final List<String> tags, 
    final List<SSUri>  discs) throws Exception{
    
    super(entityUri);
    
    this.entityUri      = entityUri;
    this.label          = label;
    this.creationTime   = creationTime;
    this.entityType     = entityType;
    this.entityDescType = entityDescType;
    this.author         = author;
    this.overallRating  = overallRating;
    
    if(tags != null){
      this.tags.addAll(tags);
    }
    
    if(discs != null){
      this.discs.addAll(discs);
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld       = new HashMap<String, Object>();
    final Map<String, Object> tagsObj  = new HashMap<String, Object>();
    final Map<String, Object> discsObj = new HashMap<String, Object>();
    
    tagsObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    tagsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.tags,      tagsObj);
    
    discsObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    discsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.discs,      discsObj);
    
    ld.put(SSVarU.entityUri,      SSVarU.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,          SSVarU.sss  + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.creationTime,   SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.entityType,     SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.entityDescType, SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.overallRating,  SSVarU.sss + SSStrU.colon + SSEntityA.class.getName()); //TODO dtheiler: change this, as its SSRatingOverall
    
    return ld;
  }
  
  /* getters to allow for json enconding */
  public String getEntityUri(){
    return SSUri.toStrWithoutSlash(entityUri);
  }

  public String getLabel() {
    return SSLabel.toStr(label);
  }

  public Long getCreationTime() {
    return creationTime;
  }

  public String getEntityType() {
    return SSEntityE.toStr(entityType);
  }

  public String getEntityDescType() {
    return SSEntityE.toStr(entityDescType);
  }
  
  public String getAuthor() throws Exception {
    return SSUri.toStrWithoutSlash(author);
  }
  
  public List<String> getTags(){
    return tags;
  }

  public SSEntityA getOverallRating(){
    return overallRating;
  }

  public List<String> getDiscs(){
    return SSUri.toStrWithoutSlash(discs);
  }
}