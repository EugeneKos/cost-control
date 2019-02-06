package org.eugene.cost.ui.card;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.eugene.cost.logic.model.card.bank.*;
import org.eugene.cost.logic.util.FileManager;
import org.eugene.cost.logic.util.StringUtil;

import javax.swing.*;

public class FinanceFXController {
    @FXML
    private ListView<Card> cards;
    @FXML
    private ListView<Cash> cashes;

    @FXML
    private TextField cardNumber;
    @FXML
    private TextField cashDescription;

    @FXML
    private TextField cardBalance;
    @FXML
    private TextField cashBalance;

    @FXML
    private Button addCard;
    @FXML
    private Button removeCard;

    @FXML
    private Button addCash;
    @FXML
    private Button removeCash;

    private BankRepository bankRepository;

    private BankFXController bankFXController;

    private Card currentCard;
    private Cash currentCash;

    private int prevLength;

    public void initialize(BankRepository bankRepository){
        this.bankRepository = bankRepository;
        initBankList();

        cards.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentCard = newValue;
            if(currentCard == null){
                cardNumber.setText("");
                cardBalance.setText("");
                return;
            }
            cardNumber.setText(currentCard.getNumber());
            cardBalance.setText(currentCard.getBalance());
        });
        cashes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentCash = newValue;
            if(currentCash == null){
                cashDescription.setText("");
                cashBalance.setText("");
                return;
            }
            cashDescription.setText(currentCash.getDescription());
            cashBalance.setText(currentCash.getBalance());
        });
        addCard.setOnAction(this::handleAddBtn);
        addCash.setOnAction(this::handleAddBtn);
        removeCard.setOnAction(this::handleRemoveBtn);
        removeCash.setOnAction(this::handleRemoveBtn);
        cardNumber.setOnKeyTyped(event -> {
            int length = cardNumber.getText().length();
            if(length == 19){
                event.consume();
            }
            if(prevLength < length &&
                    (length == 4 || length == 9 || length == 14)){
                cardNumber.appendText(" ");
            }
            prevLength = length;
        });
    }

    public void setBankFXController(BankFXController bankFXController) {
        this.bankFXController = bankFXController;
    }

    private void initBankList(){
        for (Bank bank : bankRepository.getBanks()){
            if(bank instanceof Card){
                cards.getItems().add((Card) bank);
            }
            if(bank instanceof Cash){
                cashes.getItems().add((Cash) bank);
            }
        }
    }

    private void handleAddBtn(ActionEvent event){
        Object o = event.getSource();
        if(o instanceof Button){
            Button btn = (Button) o;
            switch (btn.getId()){
                case "addCard":
                    String balance = getBalance(cardBalance);
                    if(balance == null){
                        JOptionPane.showMessageDialog(null,
                                "Не заполнен баланс новой карты!", "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    String cardNum = cardNumber.getText();
                    if(!checkCardNum(cardNum)){
                        JOptionPane.showMessageDialog(null,
                                "Номер карты не заполнен или заполнен неправильно!", "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    addBank(new Card(balance,cardNum), cards);
                    break;
                case "addCash":
                    balance = getBalance(cashBalance);
                    if(balance == null){
                        JOptionPane.showMessageDialog(null,
                                "Не заполнен баланс наличных!", "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if(cashDescription.getText().equals("")){
                        JOptionPane.showMessageDialog(null,
                                "Не заполнено описание наличных!", "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    addBank(new Cash(balance,cashDescription.getText()), cashes);
                    break;
            }
            bankFXController.initComboBox();
            bankFXController.clearBalanceAndHistory();
            FileManager.saveRepository(bankRepository);
        }
    }

    private void handleRemoveBtn(ActionEvent event){
        Object o = event.getSource();
        if(o instanceof Button){
            Button btn = (Button) o;
            switch (btn.getId()){
                case "removeCard":
                    removeBank(currentCard, cards);
                    break;
                case "removeCash":
                    removeBank(currentCash, cashes);
                    break;
            }
            bankFXController.initComboBox();
            bankFXController.clearBalanceAndHistory();
            FileManager.saveRepository(bankRepository);
        }
    }

    private <T extends Bank> void addBank(T bank, ListView<T> banks){
        if(bankRepository.addBank(bank)){
            banks.getItems().add(bank);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Невозможно добавить новую платежную систему", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkCardNum(String cardNum){
        cardNum = StringUtil.deleteSpace(cardNum);
        return cardNum.matches("[0-9]{16}");
    }

    private String getBalance(TextField balance){
        String sum = balance.getText();
        sum = StringUtil.deleteSpace(sum);
        if(StringUtil.checkSequence(sum)){
            return sum;
        }
        return null;
    }

    private void removeBank(Bank bank, ListView banks){
        if(bankRepository.removeBank(bank)){
            banks.getItems().remove(bank);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Невозможно удалить платежную систему", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
