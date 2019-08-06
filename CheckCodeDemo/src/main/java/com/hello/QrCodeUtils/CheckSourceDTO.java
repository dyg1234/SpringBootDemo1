package com.hello.QrCodeUtils;

public class CheckSourceDTO {

    private String ticket;
    private Integer dragPos;
    private Integer imageWidth;

    public CheckSourceDTO() {
    }

    public CheckSourceDTO(String ticket, Integer dragPos, Integer imageWidth) {
        this.ticket = ticket;
        this.dragPos = dragPos;
        this.imageWidth = imageWidth;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getDragPos() {
        return dragPos;
    }

    public void setDragPos(Integer dragPos) {
        this.dragPos = dragPos;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }
}
