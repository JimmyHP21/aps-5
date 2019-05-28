package aps.globalClass.messages;

import aps.globalClass.messages.bubble.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Message implements Serializable {
    private String nome;
    private MessageType type;
    private String msg;
    private int count;
    private ArrayList<User> lista;
    private ArrayList<User> usuarios;

    private Status status;
    private byte[] voiceMsg;

    private String picture;

    public byte[] getVoiceMsg() {
        return voiceMsg;
    }

    public String getPicture() {
        return picture;
    }

    public Message() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public ArrayList<User> getLista() {
        return lista;
    }

    public void setUserList(HashMap<String, User> userList){
        this.lista = new ArrayList<>(userList.values());
    }

    public void setLista(ArrayList<User> lista) {
        this.lista = lista;
    }

    public void setOnlineCount(int count) {
        this.count = count;
    }

    public int getOnlineCount() {
        return this.count;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<User> usuarios) {
        this.usuarios = usuarios;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setVoiceMsg(byte[] voiceMsg) {
        this.voiceMsg = voiceMsg;
    }


}
