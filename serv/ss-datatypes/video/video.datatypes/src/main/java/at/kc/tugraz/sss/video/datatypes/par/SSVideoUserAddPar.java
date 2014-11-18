/**
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
package at.kc.tugraz.sss.video.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "videoAdd request parameter")
public class SSVideoUserAddPar extends SSServPar{
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "video's uuid (if provided used within id if link is not set)")
  public String                uuid        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "video's link (if provided used as id)")
  public SSUri                link        = null;
  
  
//  public void setLink(final String link) throws Exception{
//    this.link = SSUri.get(link);
//  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "entity for which to attach this video")
  public SSUri                forEntity        = null;
  
  
//  public void setForEntity(final String forEntity) throws Exception{
//    this.forEntity = SSUri.get(forEntity);
//  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "video's genre")
  public String                genre        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "name")
  public SSLabel               label        = null;
  
//  public void setLabel(final String label) throws Exception{
//    this.label = SSLabel.get(label);
//  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "description")
  public SSTextComment               description        = null;
  
//  
//  public void setDescription(final String description) throws Exception{
//    this.description = SSTextComment.get(description);
//  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "creation time of the video")
  public Long               creationTime        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "latitude")
  public Double               latitude = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "longitude")
  public Double               longitude = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "accuracy")
  public Float               accuracy = null;

  public SSVideoUserAddPar(){}
  
  public SSVideoUserAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        uuid              = (String)          pars.get(SSVarU.uuid);
        link              = (SSUri)           pars.get(SSVarU.link);
        forEntity         = (SSUri)           pars.get(SSVarU.forEntity);
        genre             = (String)          pars.get(SSVarU.genre);
        label             = (SSLabel)         pars.get(SSVarU.label);
        description       = (SSTextComment)   pars.get(SSVarU.description);
        creationTime      = (Long)            pars.get(SSVarU.creationTime);
        latitude          = (Double)          pars.get(SSVarU.latitude);
        longitude         = (Double)          pars.get(SSVarU.longitude);
        accuracy          = (Float)           pars.get(SSVarU.accuracy);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          uuid =  par.clientJSONObj.get(SSVarU.app).getTextValue();
        }catch(Exception error){}
        
        try{
          link =  SSUri.get(par.clientJSONObj.get(SSVarU.link).getTextValue());
        }catch(Exception error){}
        
        try{
          forEntity =  SSUri.get(par.clientJSONObj.get(SSVarU.forEntity).getTextValue());
        }catch(Exception error){}
        
        try{
          genre =  par.clientJSONObj.get(SSVarU.genre).getTextValue();
        }catch(Exception error){}
        
        try{
          label =  SSLabel.get      (par.clientJSONObj.get(SSVarU.label).getTextValue());
        }catch(Exception error){}
        
        try{
          description =  SSTextComment.get      (par.clientJSONObj.get(SSVarU.description).getTextValue());
        }catch(Exception error){}
        
        try{
          creationTime =  par.clientJSONObj.get(SSVarU.creationTime).getLongValue();
        }catch(Exception error){}
      
        try{
          latitude =  par.clientJSONObj.get(SSVarU.latitude).getDoubleValue();
        }catch(Exception error){}
        
        try{
          longitude =  par.clientJSONObj.get(SSVarU.longitude).getDoubleValue();
        }catch(Exception error){}
        
        try{
          accuracy =  par.clientJSONObj.get(SSVarU.accuracy).getNumberValue().floatValue();
        }catch(Exception error){}
        
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getForEntity(){
    return SSStrU.removeTrailingSlash(forEntity);
  }
  
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }
}