package aps.cliente.chatwindow;


import aps.globalClass.messages.bubble.User;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.Objects;

//CLASSE PARA TRAZER A IMAGEM DO USUARIO E O SEU NOME NA LISTA
public class CellRenderer implements Callback<ListView<User>, ListCell<User>> {

    @Override
    public ListCell<User> call(ListView<User> param) {
        ListCell<User> cell = new ListCell<>() {

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();

                    Text name = new Text(user.getName());

                    ImageView statusImageView = new ImageView();
                    Image statusImage = new Image(getClass().getResource("/aps/frontEnd/images/" + user.getStatus().toString().toLowerCase() + ".png").toString(), 16, 16, true, true);
                    statusImageView.setImage(statusImage);

                    ImageView pictureImageView = new ImageView();
                    Image image = new Image(getClass().getResource("/aps/frontEnd/images/" + user.getPicture().toLowerCase() + ".png").toString(), 50, 50, true, true);
                    pictureImageView.setImage(image);

                    hBox.getChildren().addAll(statusImageView, pictureImageView, name);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}
