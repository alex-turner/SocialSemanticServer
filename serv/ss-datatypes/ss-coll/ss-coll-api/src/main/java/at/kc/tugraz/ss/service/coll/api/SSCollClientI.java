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
package at.kc.tugraz.ss.service.coll.api;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;

public interface SSCollClientI{
  
  public void collParentGet         (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collRootGet           (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collEntryDelete       (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collEntriesDelete     (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collEntryAdd          (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collEntriesAdd        (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collEntryChangePos    (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collWithEntries       (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collsWithEntries      (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collHierarchyGet      (final SSSocketCon sSCon, final SSServPar parA) throws Exception; 
  public void collCumulatedTagsGet  (final SSSocketCon sSCon, final SSServPar parA) throws Exception; 
  public void collsEntityIsInGet    (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collsCouldSubscribeGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception;
}