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
package at.kc.tugraz.ss.service.rating.impl.fct.graph;

import at.kc.tugraz.ss.serv.db.api.SSDBGraphFct;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;

public class SSRatingGraphFct extends SSDBGraphFct{
  
  public SSRatingGraphFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbGraph);
  }
  
//  public boolean setUserRating(
//    SSUri  user, 
//    int    value, 
//    SSUri  resource) throws Exception {
//    
//    SSUri  rating  = createRating();
//
//    if (db.has(null, predType(), objUser(), namedGraphUri)) {
//
//      db.add    (rating,    predType(),     createRatingUri(), namedGraphUri);
//      db.add    (resource,  predRating(),   rating, namedGraphUri);
//      db.add    (user,      predRated(),    rating, namedGraphUri);
//      db.remove (rating,    predValue(),    null, namedGraphUri);
//      db.add    (rating,    predValue(),    objValue(value), namedGraphUri);
//      
//      return true;
//    }
//    
//    return false;
//  }
//  
//  public int getUserRating(
//    SSUri user, 
//    SSUri resource) throws Exception {
//
//    int out = 0;
//
//    String queryString = 
//      selectWithParFromAndWhere(bindValue, bindRating)  + 
//      and(resource,   predRating(), bindRating)         + 
//      and(user,       predRated(),  bindRating)         +
//      and(bindRating, predValue(),  bindValue)          + 
//      whereEnd();
//      
//    for(SSQueryResultItem item : db.query(queryString)) {
//      out = Integer.parseInt(SSStrU.filterNumbers(item.getBinding(bindValue)));
//    }
//    
//    return out;
//  }
//
//  public SSRatingOverall getOverallRating(
//    SSUri resource) throws Exception {
//
//    int    rating           = 0;
//    int    numberOfRatings  = 0;
//    double result           = 0;
//    
//    String queryString = 
//      selectWithParFromAndWhere(bindValue, bindRating) + 
//      and(resource,   predRating(), bindRating)        +
//      and(bindRating, predValue(), bindValue)          + 
//      whereEnd();
//      
//    for (SSQueryResultItem item : db.query(queryString)) {
//
//      numberOfRatings++;
//      rating += Integer.parseInt(SSStrU.filterNumbers(item.getBinding(bindValue)));
//    }
//    
//    if (numberOfRatings > 0) {
//      result = rating / numberOfRatings;
//    }
//
//    return SSRatingOverall.get(result, numberOfRatings);
//  }
//  
//  private SSUri createRatingUri() throws Exception{
//    return voc.vocGraphUriForNamespace(SSVocNamespace.rating);
//  }
//  
//  private SSUri createRating() throws Exception{
//    return SSUri.get(createRatingUri().toString() + SSDateU.dateAsNano().toString());
//  }
}


//  public List<SSRating> getUserRatings(
//    SSUri user,
//    SSUri resource) throws Exception {
//
//    List<SSRating> ratingList  = new ArrayList<SSRating>();
//    String         queryString = "SELECT ?value ?rating ?page ?user FROM <" + namedGraphUri + "> WHERE{ ";
//   
//    if(SSObjectUtils.isNotNull(resource)){
//      queryString += "<" + resource + "> <" + Vocabulary.getInstance().getGraphUri(VocRelation.HAS_RATING) + "> ?rating.";
//    }
//    
//    if(SSObjectUtils.isNotNull(user)){
//      
//      queryString += "<" + user + "> <" + Vocabulary.getInstance().getGraphUri(VocRelation.RATED) + "> ?rating.";
//    }
//
//    queryString += 
//      "?user <"   + Vocabulary.getInstance().getGraphUri(VocRelation.RATED)      + "> ?rating." + 
//      "?page <"   + Vocabulary.getInstance().getGraphUri(VocRelation.HAS_RATING) + "> ?rating." + 
//      "?rating <" + Vocabulary.getInstance().getGraphUri(VocRelation.VALUE)      + "> ?value}";
//
//    SSQueryResult result = serv.prepareSparqlQuery(queryString);
//
//    for (SSQueryResultItem item : result) {
//
//      ratingList.add(
//        SSRating.get(
//        null, 
//        Integer.parseInt(item.getBinding("value").replaceAll("[^0-9]", "")),
//        SSUri.get(item.getBinding("page")),
//        SSUri.get(item.getBinding("user")),
//        0L));
//    }
//
//    return ratingList;
//  }