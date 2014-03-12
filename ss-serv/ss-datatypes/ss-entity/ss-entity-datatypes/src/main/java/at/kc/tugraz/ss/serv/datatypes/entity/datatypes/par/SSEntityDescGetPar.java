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
 package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSEntityDescGetPar extends SSServPar{
  
  public SSUri    entityUri         = null;
  public Boolean  getTags           = null;
  public Boolean  getOverallRating  = null;
  public Boolean  getDiscUris       = null;
    
  public SSEntityDescGetPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entityUri        = (SSUri)   pars.get(SSVarU.entityUri);
        getTags          = (Boolean) pars.get(SSVarU.getTags);
        getOverallRating = (Boolean) pars.get(SSVarU.getOverallRating);
        getDiscUris      = (Boolean) pars.get(SSVarU.getDiscUris);
      }
      
      if(clientPars != null){
        entityUri          = SSUri.get        (clientPars.get(SSVarU.entityUri));
        
        try{
          getTags            = Boolean.valueOf  (clientPars.get(SSVarU.getTags));
        }catch(Exception error){}
        
        try{
          getOverallRating   = Boolean.valueOf  (clientPars.get(SSVarU.getOverallRating));
        }catch(Exception error){}
        
        try{
          getDiscUris        = Boolean.valueOf  (clientPars.get(SSVarU.getDiscUris));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}