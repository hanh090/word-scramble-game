package entity;

public class WordDefinition {

	private String type;
	private String gloss;
	private String value;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGloss() {
		return gloss;
	}
	public void setGloss(String gloss) {
		this.gloss = gloss;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Word [type=" + type + ", gloss=" + gloss + ", value=" + value
				+ "]";
	}
}
