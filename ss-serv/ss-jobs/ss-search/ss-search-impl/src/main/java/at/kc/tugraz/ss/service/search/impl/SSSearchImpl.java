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
package at.kc.tugraz.ss.service.search.impl;

import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchTagsPar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEMILabel;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.solr.datatypes.SSSolrKeywordLabel;
import at.kc.tugraz.ss.service.search.api.*;
import at.kc.tugraz.ss.service.search.datatypes.*;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchMIsPar;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchSolrPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchMIsRet;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchSolrRet;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchTagsRet;
import at.kc.tugraz.ss.service.search.impl.fct.SSSearchFct;
import at.kc.tugraz.ss.service.search.impl.fct.misc.SSSearchMiscFct;
import java.util.*;

public class SSSearchImpl extends SSServImplMiscA implements SSSearchClientI, SSSearchServerI{
  
  public SSSearchImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /* SSSearchClientI */
  
  @Override
  public void searchTags(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSSearchTagsRet.get(searchTags(par), par.op));
  }
  
  @Override
  public void searchMIs(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    List<SSSearchResult> searchResults = searchMIs(par);
    
    sSCon.writeRetFullToClient(SSSearchMIsRet.get(searchResults, par.op));
  }
  
  @Override
  public void searchSolr(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSSearchSolrRet.get(searchSolr(par), par.op));
  }
  
  /* SSSearchServerI */
  
  @Override
  public List<SSSearchResult> searchTags(final SSServPar parA) throws Exception {
    
    final SSSearchTagsPar                   par                 = new SSSearchTagsPar(parA);
    final Map<String, List<SSSearchResult>> searchResultsPerTag = new HashMap<String, List<SSSearchResult>>();
    final List<SSSearchResult>              result;
    List<SSSearchResult>                    searchResultsForTagOneTag;
    
    try{
    
      for(SSTagLabel tagString : par.tags){

        searchResultsForTagOneTag = new ArrayList<SSSearchResult>();

        for(SSUri foundEntity : 
          SSServCaller.tagUserEntitiesForTagGet( 
          par.user, 
          tagString, 
          SSSpaceEnum.sharedSpace)){

          searchResultsForTagOneTag.add(
            new SSSearchResult(
            foundEntity, 
            SSSpaceEnum.sharedSpace));
        }

        for(SSUri foundEntity : 
          SSServCaller.tagUserEntitiesForTagGet(
          par.user, 
          tagString,
          SSSpaceEnum.privateSpace)){

          searchResultsForTagOneTag.add(
            new SSSearchResult(
            foundEntity,
            SSSpaceEnum.privateSpace));
        }

        searchResultsPerTag.put(SSTagLabel.toStr(tagString), searchResultsForTagOneTag);
      }
      
      result = SSSearchFct.selectAndFillSearchResults(par.user, par.searchOp, searchResultsPerTag);
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSSearchResult> searchSolr(final SSServPar parA) throws Exception {
    
    final SSSearchSolrPar                   par                        = new SSSearchSolrPar(parA);
    final Map<String, List<SSSearchResult>> searchResultsPerKeyword    = new HashMap<String, List<SSSearchResult>>();
    final List<SSSearchResult>              searchResultsForOneKeyword = new ArrayList<SSSearchResult>();
    
    try{
    
      for(SSSolrKeywordLabel keyword : par.keywords){

        searchResultsForOneKeyword.clear();
        
        for(String entityId : SSServCaller.solrSearch(keyword, 20)){

          SSSearchMiscFct.getPublicAndPrivateResults(
            par.user, 
            SSServCaller.fileUriFromID(par.user, entityId),
            searchResultsForOneKeyword);
        }

        searchResultsPerKeyword.put(SSSolrKeywordLabel.toStr(keyword), searchResultsForOneKeyword);
      }
      
      return SSSearchFct.selectAndFillSearchResults(par.user, par.searchOp, searchResultsPerKeyword);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSSearchResult> searchMIs(final SSServPar parA) throws Exception {
    
    final SSSearchMIsPar                    par                        = new SSSearchMIsPar(parA);
    final Map<String, List<SSSearchResult>> searchResultsPerKeyword    = new HashMap<String, List<SSSearchResult>>();
    final List<SSSearchResult>              searchResultsForOneKeyword = new ArrayList<SSSearchResult>();
    
    try{
      
      for(SSModelUEMILabel mi : par.mIs){
        
        searchResultsForOneKeyword.clear();
        
        for(SSUri entityUri : SSServCaller.modelUEEntitiesForMiGet(par.user, mi)){
         
          SSSearchMiscFct.getPublicAndPrivateResults(
            par.user,
            entityUri,
            searchResultsForOneKeyword);
        }

        searchResultsPerKeyword.put(SSModelUEMILabel.toStr(mi), searchResultsForOneKeyword);
      }
      
      return SSSearchFct.selectAndFillSearchResults(par.user, par.searchOp, searchResultsPerKeyword);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}