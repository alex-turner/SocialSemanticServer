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
package at.kc.tugraz.ss.serv.job.dataexport.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.List;
import java.util.Map;

public class SSDataExportUserEntityTagCategoriesPar extends SSServPar{
  
  public String                      fileName               = null;
  public Map<String, List<String>>   tagsPerEntities        = null;
  public Map<String, List<String>>   categoriesPerEntities  = null;
  public Boolean                     wasLastLine            = null;
  
  public SSDataExportUserEntityTagCategoriesPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        this.fileName              = (String)                     pars.get(SSVarU.fileName);
        this.tagsPerEntities       = (Map<String, List<String>>)  pars.get(SSVarU.tagsPerEntities);
        this.categoriesPerEntities = (Map<String, List<String>>)  pars.get(SSVarU.categoriesPerEntities);
        this.wasLastLine           = (Boolean)                    pars.get(SSVarU.wasLastLine);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}