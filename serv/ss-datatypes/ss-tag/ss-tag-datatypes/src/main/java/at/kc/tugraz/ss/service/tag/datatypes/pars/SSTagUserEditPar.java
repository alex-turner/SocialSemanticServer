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
 package at.kc.tugraz.ss.service.tag.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSTagUserEditPar extends SSServPar{
  
  public SSUri           tag     = null;
  public SSTagLabel      label   = null;
  
  public SSTagUserEditPar(
    final SSMethU    op,
    final String     key, 
    final SSUri      user, 
    final SSUri      tag, 
    final SSTagLabel label){
    
    super(op, key, user);
    
    this.tag   = tag;
    this.label = label;
  }
  
  public SSTagUserEditPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        tag      = (SSUri)                pars.get(SSVarU.tag);
        label    = SSTagLabel.get((String)pars.get(SSVarU.label));
      }
      
      if(par.clientJSONObj != null){
        
        tag       = SSUri.get        (par.clientJSONObj.get(SSVarU.tag).getTextValue());
        label     = SSTagLabel.get   (par.clientJSONObj.get(SSVarU.label).getTextValue());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getTag(){
    return SSStrU.removeTrailingSlash(tag);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
}
