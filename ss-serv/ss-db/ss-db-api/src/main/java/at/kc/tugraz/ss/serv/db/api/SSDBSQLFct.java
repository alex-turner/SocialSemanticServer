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
package at.kc.tugraz.ss.serv.db.api;

import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import java.sql.ResultSet;

public class SSDBSQLFct extends SSDBFct{

  protected        final SSDBSQLI dbSQL;
  protected static final String   circleTable                         = "circle";
  protected static final String   circleUsersTable                    = "circleusers";
  protected static final String   circleEntitiesTable                 = "circleentities";
  protected static final String   collTable                           = "coll";
  protected static final String   collRootTable                       = "collroot";
  protected static final String   collSpecialTable                    = "collspecial";
  protected static final String   collEntryPosTable                   = "collentrypos";
  protected static final String   collHierarchyTable                  = "collhierarchy";
  protected static final String   collUserTable                       = "colluser";
  protected static final String   entityTable                         = "entity";

  public SSDBSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super();
    
    this.dbSQL = dbSQL;
  }
  
  public SSUri bindingStrToUri(ResultSet resultSet, String binding) throws Exception{
    return SSUri.get(bindingStr(resultSet, binding));
  }
  
  public SSSpaceE bindingStrToSpace(ResultSet resultSet, String binding) throws Exception{
    return SSSpaceE.get(bindingStr(resultSet, binding));
  }
  
  public String bindingStr(ResultSet resultSet, String binding) throws Exception{
    return resultSet.getString(binding);
  }
  
  public SSLabel bindingStrToLabel(ResultSet resultSet, String binding) throws Exception{
    return SSLabel.get(bindingStr(resultSet, binding));
  }
  
  public SSEntityE bindingStrToEntityType(ResultSet resultSet, String binding) throws Exception{
    return SSEntityE.valueOf(bindingStr(resultSet, binding));
  }
  
  public Float bindingStrToFloat(ResultSet resultSet, String binding) throws Exception{
    return Float.parseFloat(bindingStr(resultSet, binding));
  }
  
  public Integer bindingStrToInteger(ResultSet resultSet, String binding) throws Exception{
    return Integer.parseInt(bindingStr(resultSet, binding));
  }
  
  public Long bindingStrToLong(ResultSet resultSet, String binding) throws Exception{
    return Long.parseLong(bindingStr(resultSet, binding));
  }
}
