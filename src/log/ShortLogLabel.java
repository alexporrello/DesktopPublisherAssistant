package log;

public enum ShortLogLabel {
	JIRA_TICKET_DESCRIPTION(0), CREATED_DATE(1), JIRA_TICKET_REPORTER(2), 
	PART_NUM_32(3), PART_NUM_37(4), STATUS(5);
	
	public int i;
	
	ShortLogLabel(int i) {
		this.i = i;
	}
}