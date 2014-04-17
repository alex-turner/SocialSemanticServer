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
package at.kc.tugraz.ss.serv.jobs.evernote.impl.helper;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import java.util.List;

public class SSEvernoteUEHelper {
  
  public void addUEsForNormalNotebook(
    SSUri userUri,
    SSUri notebookUri,
    Notebook notebook) throws Exception {
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      notebookUri,
      SSUEEnum.evernoteNotebookCreate,
      SSStrU.empty,
      notebook.getServiceCreated() + SSEvernoteTimestampCounter.get(),
      false);
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      notebookUri,
      SSUEEnum.evernoteNotebookUpdate,
      SSStrU.empty,
      notebook.getServiceUpdated()  + SSEvernoteTimestampCounter.get(),
      false);
  }
  
  public void addUEsForSharedNotebook(
    Boolean isSharedNotebook,
    SSUri userUri,
    Notebook notebook,
    SSUri notebookUri,
    List<SharedNotebook> sharedNotebooks) throws Exception {
    
    if(!isSharedNotebook){
      return;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      
      if(notebook.getGuid().equals(sharedNotebook.getNotebookGuid())){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          notebookUri,
          SSUEEnum.evernoteNotebookShare,
          SSStrU.empty,
          sharedNotebook.getServiceCreated() + SSEvernoteTimestampCounter.get(),
          false);
      }
    }
  }
  
  public void addUEsAndTagsForNormalNote(
    SSUri userUri,
    Note note,
    SSUri noteUri) throws Exception {
    
    if(note == null){
      return;
    }
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      noteUri,
      SSUEEnum.evernoteNoteCreate,
      SSStrU.empty,
      note.getCreated() + SSEvernoteTimestampCounter.get(),
      false);
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      noteUri,
      SSUEEnum.evernoteNoteUpdate,
      SSStrU.empty,
      note.getUpdated() + SSEvernoteTimestampCounter.get(),
      false);
    
    if(note.getDeleted() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteNoteDelete,
        SSStrU.empty,
        note.getDeleted() + SSEvernoteTimestampCounter.get(),
        false);
    }
    
    if(
      note.getTagNames()  != null &&
      !note.getTagNames().isEmpty()){
      
      SSServCaller.addTagsAtCreationTime(
        userUri,
        noteUri,
        SSTagLabel.getDistinct(note.getTagNames()),
        SSSpaceEnum.privateSpace,
        note.getUpdated() + SSEvernoteTimestampCounter.get(),
        false);
      
      for(String tagName : note.getTagNames()){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          noteUri,
          SSUEEnum.addPrivateTag,
          tagName,
          note.getUpdated() + SSEvernoteTimestampCounter.get(),
          false);
      }
    }
  }
  
  public void addUEsForSharedNote(
    Boolean isSharedNotebook,
    SSUri userUri,
    Notebook notebook,
    SSUri noteUri,
    List<SharedNotebook> sharedNotebooks) throws Exception {
    
    if(
      !isSharedNotebook ||
      notebook == null){
      return;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      
      if(notebook.getGuid().equals(sharedNotebook.getNotebookGuid())){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          noteUri,
          SSUEEnum.evernoteNoteShare,
          SSStrU.empty,
          sharedNotebook.getServiceCreated() + SSEvernoteTimestampCounter.get(),
          false);
      }
    }
  }
  
  public void addUEsForNoteAttrs(
    SSUri userUri,
    Note note,
    SSUri noteUri) throws Exception {
    
    NoteAttributes noteAttr;
    
    if(
      note     == null ||
      noteUri  == null){
      return;
    }
    
    noteAttr = note.getAttributes();
    
    if(noteAttr == null){
      return;
    }
    
    if(noteAttr.getShareDate() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteNoteShare,
        SSStrU.empty,
        noteAttr.getShareDate(),
        false);
    }
    
    if(noteAttr.getReminderDoneTime() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteReminderDone,
        SSStrU.empty,
        noteAttr.getReminderDoneTime(),
        false);
    }
    
    if(noteAttr.getReminderTime() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteReminderCreate,
        SSStrU.empty,
        noteAttr.getReminderTime(),
        false);
    }
  }
  
  public void addUEsForResource(
    SSUri userUri,
    SSUri resourceUri,
    Note note) throws Exception{
    
    if(
      resourceUri == null ||
      note        == null){
      return;
    }
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      resourceUri,
      SSUEEnum.evernoteResourceAdd,
      SSStrU.empty,
      note.getUpdated(),
      false);
  }
  
  public void addUEsForSharedResource(
    SSUri userUri,
    SSUri resourceUri,
    Notebook notebook,
    List<SharedNotebook> sharedNotebooks,
    Boolean isSharedNotebook) throws Exception{
    
    if(
      !isSharedNotebook ||
      notebook        == null ||
      sharedNotebooks == null){
      return;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      
      if(notebook.getGuid().equals(sharedNotebook.getNotebookGuid())){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          resourceUri,
          SSUEEnum.evernoteResourceShare,
          SSStrU.empty,
          sharedNotebook.getServiceCreated(),
          false);
      }
    }
  }
  
  public void addUEsForLinkedNotebook(
    SSUri userUri,
    SSUri notebookUri,
    Long creationTimeForLinkedNotebook) throws Exception {
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      notebookUri,
      SSUEEnum.evernoteNotebookFollow,
      SSStrU.empty,
      creationTimeForLinkedNotebook,
      false);
  }
}
