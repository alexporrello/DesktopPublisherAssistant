package ticket;

public enum TicketInfo {
	TITLE(0), 
	PART_NUM_32(1), PART_NUM_37(2),
	DATE(3), GUID(4), PERFORCE_URL(5),
	TICKET_DESCRIPTION(6), JIRA_TICKET_URL(7),
	TCIS_URL(8),
	STATUS(9),
	REPORT(10),
	JIRA_TICKET_KEY(11);

	public static final int NUM_ENTRIES = 12;
	
	public int i;

	TicketInfo(int i) {
		this.i = i;
	}
}
