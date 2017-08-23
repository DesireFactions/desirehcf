package me.borawski.hcf.tickets;

import java.util.UUID;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

public class Ticket {

    @Id
    private int id;

    @Indexed
    private UUID player;

    private long opened;

    private String text;

    @Indexed
    private UUID closer;

    private long closed;

    private String response;

    private Status status;

    /**
     * Used exclusively to make a new Ticket. Should never be used on a
     * non-existing ticket.
     * 
     * @param player
     *            the player who opened the ticket
     * @param text
     *            the text of the ticket
     */
    public Ticket(UUID player, String text) {
        this.player = player;
        this.opened = System.currentTimeMillis();
        this.text = text;
        this.status = Status.OPEN;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public long getOpened() {
        return opened;
    }

    public void setOpened(long opened) {
        this.opened = opened;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getCloser() {
        return closer;
    }

    public void setCloser(UUID closer) {
        this.closer = closer;
    }

    public long getClosed() {
        return closed;
    }

    public void setClosed(long closed) {
        this.closed = closed;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private static enum Status {
        OPEN,
        CLOSED,
        DELETED;
    }

}
