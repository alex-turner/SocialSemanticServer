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
package at.kc.tugraz.ss.activity.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSActivitySQLFct extends SSDBSQLFct{

  public SSActivitySQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public SSUri createActivityURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objActivity().toString()));
  }
  
  private static SSUri objActivity() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.activity.toString());
  }  

  public void addActivity(
    final SSUri               activityUri, 
    final SSActivityE         type, 
    final List<SSUri>         userUris, 
    final List<SSUri>         sourceEntityUris, 
    final List<SSUri>         targetEntityUris, 
    final List<SSTextComment> textComments) throws Exception{
    
    try{
      
      if(SSObjU.isNull(activityUri, type)){
        throw new Exception("pars null");
      }
      
      final Map<String, String> insert    = new HashMap<String, String>();
      
      insert.put   (SSSQLVarU.activityId,     SSUri.toStr(activityUri));
      insert.put   (SSSQLVarU.activityType,   SSActivityE.toStr(type));
      
      if(!textComments.isEmpty()){
        insert.put   (SSSQLVarU.textComment,   SSTextComment.toStr(textComments.get(0)));
      }else{
        insert.put   (SSSQLVarU.textComment,   SSStrU.empty);
      }
      
      dbSQL.insert(activityTable, insert);
      
      for(SSUri userUri : userUris){

        insert.clear();
        insert.put(SSSQLVarU.activityId,     SSUri.toStr(activityUri));
        insert.put(SSSQLVarU.userId,         SSUri.toStr(userUri));
        
        dbSQL.insert(activityUsersTable, insert);
      }
      
      for(SSUri sourceEntityUri : sourceEntityUris){

        insert.clear();
        insert.put(SSSQLVarU.activityId,     SSUri.toStr(activityUri));
        insert.put(SSSQLVarU.entityId,       SSUri.toStr(sourceEntityUri));
        
        dbSQL.insert(activitySourceEntitiesTable, insert);
      }
      
      for(SSUri targetEntityUri : targetEntityUris){

        insert.clear();
        insert.put(SSSQLVarU.activityId,     SSUri.toStr(activityUri));
        insert.put(SSSQLVarU.entityId,       SSUri.toStr(targetEntityUri));
        
        dbSQL.insert(activityTargetEntitiesTable, insert);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}