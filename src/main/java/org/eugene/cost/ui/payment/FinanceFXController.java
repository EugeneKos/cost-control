package org.eugene.cost.ui.payment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.eugene.cost.data.model.Bank;
import org.eugene.cost.data.model.Card;
import org.eugene.cost.data.model.Cash;
import org.eugene.cost.data.repository.BankRepository;
import org.eugene.cost.util.FileManager;
import org.eugene.cost.util.StringUtil;

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

    @FXML
    private DatePicker cardsDate;
    @FXML
    private DatePicker cashesDate;

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
            if (clear(currentCard == null, cardNumber, cardBalance, cardsDate)) return;
            cardNumber.setText(currentCard.getNumber());
            cardBalance.setText(currentCard.getBalance());
            cardsDate.setValue(currentCard.getDate());
        });
        cashes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentCash = newValue;
            if (clear(currentCash == null, cashDescription, cashBalance, cashesDate)) return;
            cashDescription.setText(currentCash.getDescription());
            cashBalance.setText(currentCash.getBalance());
            cashesDate.setValue(currentCash.getDate());
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

    private boolean clear(boolean condition, TextField description, TextField balance, DatePicker date) {
        if(condition){
            description.setText("");
            balance.setText("");
            date.setValue(null);
            return true;
        }
        return false;
    }

    public void setBankFXController(BankFXController bankFXController) {
        this.bankFXController = bankFXController;
    }

    private void initBankList(){
        BankFXController.fillList(bankRepository, cards.getItems(), cashes.getItems());
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
                                "Не заполнен баланс новой карты!",
                                "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    String cardNum = cardNumber.getText();
                    if(!checkCardNum(cardNum)){
                        JOptionPane.showMessageDialog(null,
                                "Номер карты не заполнен или заполнен неправильно!",
                                "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if(cardsDate.getValue() == null){
                        addBank(new Card(balance,cardNum), cards);
                    } else {
                        addBank(new Card(balance,cardNum, cardsDate.getValue()), cards);
                    }
                    break;
                case "addCash":
                    balance = getBalance(cashBalance);
                    if(balance == null){
                        JOptionPane.showMessageDialog(null,
                                "Не заполнен баланс наличных!",
                                "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if(cashDescription.getText().equals("")){
                        JOptionPane.showMessageDialog(null,
                                "Не заполнено описание наличных!",
                                "Информация", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if(cashesDate.getValue() == null){
                        addBank(new Cash(balance,cashDescription.getText()), cashes);
                    } else {
                        addBank(new Cash(balance,cashDescription.getText(), cashesDate.getValue()), cashes);
                    }
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
                    "Невозможно добавить новую платежную систему",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
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
