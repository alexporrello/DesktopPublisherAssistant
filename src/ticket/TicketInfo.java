package ticket;

public enum TicketInfo {
	TITLE(0, "Document Title"), 
	PART_NUM_32(1, "32 Part Number"), PART_NUM_37(2, "37 Part Number"),
	DATE(3, "Date"), GUID(4, "GUID"), PERFORCE_URL(5, "Perforce URL"),
	TICKET_DESCRIPTION(6, "Jira Ticket Description"), JIRA_TICKET_URL(7, "Jira Ticket URL"),
	TCIS_URL(8, "TCIS URL"),
	STATUS(9, "Jira Ticket Status"),
	REPORT(10, "Jira Ticket Report"),
	JIRA_TICKET_KEY(11, "Jira Ticket Key");

	public static final int NUM_ENTRIES = 12;
	
	public int i;
	public String name;

	TicketInfo(int i, String name) {
		this.i = i;
		this.name = name;
	}
}
