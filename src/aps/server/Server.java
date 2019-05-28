package aps.server;

import aps.globalClass.excpetions.DuplicateUsernameExcpetion;
import aps.globalClass.messages.Message;
import aps.globalClass.messages.MessageType;
import aps.globalClass.messages.Status;
import aps.globalClass.messages.bubble.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
    private static final int PORT = 9001;
    private static final HashMap<String, User> nomes = new HashMap<>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static ArrayList<User> users = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(Server.class);



    public static void main(String[] args) throws Exception {
        logger.info("SERVIDOR EM FUNCIONAMENTO");
        ServerSocket receptor = new ServerSocket(PORT);

        try {
            while (true) {
                new Handler(receptor.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            receptor.close();
        }
    }

    private static class Handler extends Thread{

        private String nome;
        private Socket socket;
        private User user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;

        public Handler(Socket socket) throws IOException {
            this.socket = socket;
        }

        public void run() {
            logger.info("Attempting to connect a user...");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                checkDuplicateUsername(firstMessage);
                writers.add(output);
                sendNotification(firstMessage);
                addToList();

                while (socket.isConnected()) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        logger.info(inputmsg.getType() + " - " + inputmsg.getNome() + ": " + inputmsg.getMsg());
                        switch (inputmsg.getType()) {
                            case USER:
                                escritor(inputmsg);
                                break;
                            case VOICE:
                                escritor(inputmsg);
                                break;
                            case CONNECTED:
                                addToList();
                                break;
                            case STATUS:
                                changeStatus(inputmsg);
                                break;
                        }
                    }
                }
            } catch (SocketException socketException) {
                logger.error("Socket Exception for user " + nome);
            } catch (DuplicateUsernameExcpetion duplicateException){
                logger.error("Duplicate Username : " + nome);
            } catch (Exception e){
                logger.error("Exception in run() method for user: " + nome, e);
            } finally {
                closeConnections();
            }
        }

        private synchronized void checkDuplicateUsername(Message firstMessage) throws DuplicateUsernameExcpetion {
            logger.info(firstMessage.getNome() + " is trying to connect");
            if (!nomes.containsKey(firstMessage.getNome())) {
                this.nome = firstMessage.getNome();
                user = new User();
                user.setName(firstMessage.getNome());
                user.setStatus(Status.ONLINE);
                user.setPicture(firstMessage.getPicture());

                users.add(user);
                nomes.put(nome, user);

                logger.info(nome + " has been added to the list");
            } else {
                logger.error(firstMessage.getNome() + " is already connected");
                throw new DuplicateUsernameExcpetion(firstMessage.getNome() + " is already connected");
            }
        }

        private Message changeStatus(Message inputmsg) throws IOException {
            logger.debug(inputmsg.getNome() + "ALTEROU O STATUS PARA" + inputmsg.getStatus());
            Message msg = new Message();
            msg.setNome(user.getName());
            msg.setType(MessageType.STATUS);
            msg.setMsg("");
            User usuarioObj = nomes.get(nome);
            usuarioObj.setStatus(inputmsg.getStatus());
            escritor(msg);
            return msg;
        }

        private Message sendNotification(Message firstMessage) throws IOException {
            Message msg = new Message();
            msg.setMsg("Entrou no chat");
            msg.setType(MessageType.NOTIFICATION);
            msg.setNome(firstMessage.getNome());
            msg.setPicture(firstMessage.getPicture());
            escritor(msg);
            return msg;
        }

        private Message removeFromList() throws IOException {
            logger.debug("removeFromList() método Enter");
            Message msg = new Message();
            msg.setMsg("Saiu do chat.");
            msg.setType(MessageType.DISCONNECTED);
            msg.setNome("SERVER");
            msg.setUserList(nomes);
            escritor(msg);
            logger.debug("removeFromList() método Sair");
            return msg;
        }

        /*
         * Mostra que o usuario entrou no server
         */
        private Message addToList() throws IOException {
            Message msg = new Message();
            msg.setMsg("Bem vindo, você se conectou no servidor!");
            msg.setType(MessageType.CONNECTED);
            msg.setNome("SERVER");
            escritor(msg);
            return msg;
        }

        /*
         * Criando e enviando a MessageType para o Listeners
         */
        private void escritor(Message msg) throws IOException {
            for (ObjectOutputStream escritor  : writers){
                msg.setUserList(nomes);
                msg.setUsuarios(users);
                msg.setOnlineCount(nomes.size());
                escritor.writeObject(msg);
                escritor.reset();
            }
        }

        private synchronized void closeConnections()  {
            logger.debug("closeConnections() method Enter");
            logger.info("HashMap names:" + nomes.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            if (nome != null) {
                nomes.remove(nome);
                logger.info("User: " + nome + " has been removed!");
            }
            if (user != null){
                users.remove(user);
                logger.info("User object: " + user + " has been removed!");
            }
            if (output != null){
                writers.remove(output);
                logger.info("Writer object: " + user + " has been removed!");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("HashMap names:" + nomes.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            logger.debug("closeConnections() method Exit");
        }
    }

}
