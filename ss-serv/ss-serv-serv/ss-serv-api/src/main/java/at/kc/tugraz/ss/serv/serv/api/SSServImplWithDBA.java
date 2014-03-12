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
package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;

public abstract class SSServImplWithDBA extends SSServImplA{
  
  public final SSDBSQLI   dbSQL;
  public final SSDBGraphI dbGraph;

  public SSServImplWithDBA(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL){
    
    super(conf);
    
    this.dbSQL   = dbSQL;
    this.dbGraph = dbGraph;
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
//    ((SSServImplDBA)dbSQL).finalizeImpl();
  }
}