package org.argus.core.route;
public class Artifact {

	private String groupId;

	private String artifactId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	@Override
	public int hashCode()
	{
		return groupId.hashCode() + artifactId.hashCode();
	}
	
	@Override 
	public boolean equals(Object object)
	{
		if(!(object instanceof Artifact))
		{
			return false;
		}
		
		Artifact artifact = (Artifact)object;
		if(this.groupId.equals(artifact.getGroupId()) && this.artifactId.equals(artifact.getArtifactId()))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "GroupId=[" + groupId + "] ArtifactId=[" + artifactId + "]";
	}
	
}
