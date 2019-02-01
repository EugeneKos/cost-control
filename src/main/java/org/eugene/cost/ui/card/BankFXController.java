package org.eugene.cost.ui.card;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.eugene.cost.App;
import org.eugene.cost.logic.model.card.bank.Bank;
import org.eugene.cost.logic.model.card.bank.BankRepository;
import org.eugene.cost.logic.model.card.bank.Card;
import org.eugene.cost.logic.model.card.bank.Cash;
import org.eugene.cost.logic.model.card.op.Debit;
import org.eugene.cost.logic.model.card.op.Operation;

public class BankFXController {
    @FXML
    private ListView<Operation> operations;

    @FXML
    private Button operationControl;
    @FXML
    private Button financeControl;
    @FXML
    private Button limitControl;

    @FXML
    private Label cashBalance;
    @FXML
    private Label cardBalance;
    @FXML
    private Label bankType;

    @FXML
    private ImageView imageCard;
    @FXML
    private ImageView imageCash;

    @FXML
    private ComboBox<Card> cardBox;
    @FXML
    private ComboBox<Cash> cashBox;

    private App app;

    private Card currentCard;
    private Cash currentCash;

    private BankRepository bankRepository = new BankRepository();

    private void initBankRepository(){
        bankRepository.addBank(new Card("20547.34","0000 1111 3333 4444"));
        bankRepository.addBank(new Cash("10765.58","Заначка"));
    }

    @FXML
    public void initialize(){
        initBankRepository();
        initComboBox();
        cardBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentCard = newValue;
            updateBalanceAndHistory();
        });
        cashBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentCash = newValue;
            updateBalanceAndHistory();
        });
        operations.getItems().add(new Debit("250","AAAA"));
        imageCard.setOnMouseClicked(this::handleImageCard);
        imageCash.setOnMouseClicked(this::handleImageCash);
        operationControl.setOnAction(this::handleOperationBtn);
        financeControl.setOnAction(this::handleFinanceBtn);
        limitControl.setOnAction(this::handleLimitBtn);
    }

    public void setApp(App app) {
        this.app = app;
    }

    private void initComboBox(){
        for (Bank bank : bankRepository.getBanks()){
            if(bank instanceof Card){
                cardBox.getItems().add((Card) bank);
            }
            if(bank instanceof Cash){
                cashBox.getItems().add((Cash) bank);
            }
        }
    }

    private void handleImageCard(MouseEvent event){
        bankType.setText("карты");
        imageAnimation(imageCard);
        updateBalanceAndHistory();
    }

    private void handleImageCash(MouseEvent event){
        bankType.setText("наличных");
        imageAnimation(imageCash);
        updateBalanceAndHistory();
    }

    private void imageAnimation(ImageView image){
        Thread thread = new Thread(()->{
            int step = 3;
            boolean down = true;
            boolean left = true;
            for (int i=0; i < 27; i++){
                if(image.getRotate() >= 0 & image.getRotate() < 21 & left){
                    image.setRotate(image.getRotate()+step);
                    down = true;
                } else if(down) {
                    left = false;
                    image.setRotate(image.getRotate()-step);
                }
                if(image.getRotate() < 0 & image.getRotate() > -21 & !left){
                    image.setRotate(image.getRotate()-step);
                    down = false;
                } else if(!down) {
                    left = true;
                    image.setRotate(image.getRotate()+step);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void handleLimitBtn(ActionEvent event){
        app.openLimit();
    }

    private void handleOperationBtn(ActionEvent event){
        app.openOperation(bankRepository.getBanks(), this);
        updateBalanceAndHistory();
    }

    private void handleFinanceBtn(ActionEvent event){

    }

    public void updateBalanceAndHistory(){
        if(currentCard != null){
            cardBalance.setText(currentCard.getBalance() + " Руб.");
        }
        if(currentCash != null){
            cashBalance.setText(currentCash.getBalance() + " Руб.");
        }
        if(bankType.getText().equals("карты") & currentCard != null){
            updateHistory(currentCard);
        } else if(currentCash != null) {
            updateHistory(currentCash);
        }
    }

    private void updateHistory(Bank bank){
        operations.getItems().clear();
        for (Operation operation : bank.getOperationHistory()){
            operations.getItems().add(operation);
        }
    }
}
