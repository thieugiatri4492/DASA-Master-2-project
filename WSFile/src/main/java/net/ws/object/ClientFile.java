package net.ws.object;

import java.io.Reader;
import java.util.List;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.File.Labels;
import com.google.api.services.drive.model.ParentReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class ClientFile {
	 public String resource_id;
	 public String title;
	 public String description;
	 public String mimeType;
	 public boolean editable;
	 public Labels labels;
	 public List<ParentReference> parents;
	 public String content;
	 
	 public ClientFile() {}
	 
	 public ClientFile(File file, String content) {
		    this.resource_id = file.getId();
		    this.title = file.getTitle();
		    this.description = file.getDescription();
		    this.mimeType = file.getMimeType();
		    this.content = content;
		    this.labels = file.getLabels();
		    this.editable = file.getEditable();
		    this.parents = file.getParents();
		  }
	 public ClientFile(Reader in) {
		    GsonBuilder builder = new GsonBuilder();
		    Gson gson = builder.create();
		    ClientFile other = gson.fromJson(in, ClientFile.class);
		    this.resource_id = other.resource_id;
		    this.title = other.title;
		    this.description = other.description;
		    this.mimeType = other.mimeType;
		    this.content = other.content;
		    this.labels = other.labels;
		    this.editable = other.editable;
		    this.parents = other.parents;
		  }
	 public File toFile() {
		    File file = new File();
		    file.setId(resource_id);
		    file.setTitle(title);
		    file.setDescription(description);
		    file.setMimeType(mimeType);
		    file.setLabels(labels);
		    file.setEditable(editable);
		    file.setParents(parents);
		    return file;
		  }

}
