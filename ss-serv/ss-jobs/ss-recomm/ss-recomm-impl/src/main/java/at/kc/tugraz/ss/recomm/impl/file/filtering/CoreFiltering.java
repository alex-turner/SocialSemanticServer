package at.kc.tugraz.ss.recomm.impl.file.filtering;

import at.kc.tugraz.ss.recomm.impl.common.UserData;
import at.kc.tugraz.ss.recomm.impl.file.WikipediaReader;
import java.util.ArrayList;
import java.util.HashSet;

public class CoreFiltering {

	private WikipediaReader reader;
	
	public CoreFiltering(WikipediaReader reader) {
		this.reader = reader;
	}
	
	public WikipediaReader filterOrphansIterative(int level) {
		return filterOrphansIterative(level, level, level);
	}
	
	public WikipediaReader filterOrphansIterative(int userLevel, int resLevel, int tagLevel) {	
		HashSet<Integer> userIDs = new HashSet<Integer>();
		for (int i = 0; i < this.reader.getUsers().size(); i++) {
			int count = this.reader.getUserCounts().get(i);
			if (count >= userLevel) {
				userIDs.add(i);
			}
		}
		System.out.println("User IDs determined ...");
		
		HashSet<Integer> resIDs = new HashSet<Integer>();
		for (int i = 0; i < this.reader.getResources().size(); i++) {
			int count = this.reader.getResourceCounts().get(i);
			if (count >= resLevel) {
				resIDs.add(i);
			}
		}
		System.out.println("Res IDs determined ...");
		
		HashSet<Integer> tagIDs = new HashSet<Integer>();
		if (tagLevel > 0) {
			for (int i = 0; i < this.reader.getTags().size(); i++) {
				int count = this.reader.getTagCounts().get(i);
				if (count >= tagLevel) {
					tagIDs.add(i);
				}
			}
			System.out.println("Tag IDs determined ...");
		}

		System.out.println("Start removing ...");
		ArrayList<UserData> keepData = new ArrayList<UserData>();
		for (UserData data : this.reader.getUserLines()) {
			int resID = data.getWikiID();
			int userID = data.getUserID();
			if (resIDs.contains(resID) && userIDs.contains(userID)) {
				if (tagLevel > 0) {
					ArrayList<Integer> tags = new ArrayList<Integer>();
					for (Integer tag : data.getTags()) {
						if (tagIDs.contains(tag)) {
							tags.add(tag);
						}
					}
					if (tags.size() > 0) {
						data.setTags(tags);
						keepData.add(data);
					}
				} else {
					keepData.add(data);
				}
			}
		}
		
		System.out.println("Kept lines: " + keepData.size());
		this.reader.setUserLines(keepData);
		return this.reader;
	}
}
