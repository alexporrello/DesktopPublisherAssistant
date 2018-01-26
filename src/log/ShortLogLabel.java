package log;

public enum ShortLogLabel {
	JIRA_TICKET_DESCRIPTION(0), JIRA_TICKET_REPORTER(1), 
	PART_NUM_32(2), PART_NUM_37(3);
	
	public int i;
	
	ShortLogLabel(int i) {
		this.i = i;
	}
}