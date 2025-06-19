package client.gui.controllers;

import client.ConnectionHandler;
import client.LoginManager;
import client.gui.components.ObjectCanvas;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import models.Ticket;
import models.TicketType;
import models.Venue;
import models.VenueType;
import utils.CommandException;
import client.cmd.Insert;
import client.cmd.Remove;
import client.cmd.Show;
import client.cmd.UpdateId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainController {

    @FXML private Label usernameLabel;
    @FXML private TextField filterField;
    @FXML private Label errorLabel;
    @FXML private Label infoLabel;

    @FXML private TableView<Ticket> ticketTable;
    @FXML private TableColumn<Ticket, Long> idColumn;
    @FXML private TableColumn<Ticket, String> nameColumn;
    @FXML private TableColumn<Ticket, Double> xColumn;
    @FXML private TableColumn<Ticket, Double> yColumn;
    @FXML private TableColumn<Ticket, String> creationDateColumn;
    @FXML private TableColumn<Ticket, Float> priceColumn;
    @FXML private TableColumn<Ticket, Double> discountColumn;
    @FXML private TableColumn<Ticket, Boolean> refundableColumn;
    @FXML private TableColumn<Ticket, String> typeColumn;

    @FXML private TableColumn<Ticket, Long> venueIdColumn;
    @FXML private TableColumn<Ticket, String> venueNameColumn;
    @FXML private TableColumn<Ticket, Integer> venueCapacityColumn;
    @FXML private TableColumn<Ticket, String> venueTypeColumn;
    @FXML private TableColumn<Ticket, Void> actionColumn = new TableColumn<>();
    @FXML private TableColumn<Ticket, Void> cancelColumn = new TableColumn<>();
    @FXML private TableColumn<Ticket, Void> trashColumn = new TableColumn<>();
    //buttons
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button logoutButton;

    @FXML private TextArea ticketInfoArea;

    @FXML private Canvas canvas;
    private ObjectCanvas objectCanvas;

    private FilteredList<Ticket> filteredData;
    private Collection<Ticket> tickets;
    private ConnectionHandler chandler;
    private LoginManager loginManager;

    private Ticket selectedTicket;
    private boolean isEditMode = false;
    private boolean isTrashMode = false;
    private List<Ticket> originalUserTickets = new ArrayList<>();
    private ObservableList<Ticket> masterData = FXCollections.observableArrayList();

    public void init(ConnectionHandler chandler, LoginManager loginManager) {
        this.loginManager = loginManager;
        this.chandler = chandler;

        usernameLabel.setText("Текущий пользователь: " + loginManager.getCurrentUser().getUsername());
        errorLabel.setText(""); // пустое сообщение

        // Привязка колонок
        idColumn.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        xColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getCoordinates().getX()).asObject());
        yColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getCoordinates().getY()).asObject());
        creationDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCreationDate().toString()));
        priceColumn.setCellValueFactory(cell -> new SimpleFloatProperty(cell.getValue().getPrice()).asObject());
        discountColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getDiscount()).asObject());
        refundableColumn.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getRefundable()).asObject());
        typeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType().name()));

        venueIdColumn.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getVenue().getId()).asObject());
        venueNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVenue().getName()));
        venueCapacityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getVenue().getCapacity()).asObject());
        venueTypeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVenue().getType().name()));

        actionColumn.setCellFactory(col -> new TableCell<>() {
        private final Button confirmButton = new Button("✔");
        {
            confirmButton.setOnAction(e -> {
                Ticket ticket = getTableView().getItems().get(getIndex());
                System.out.println(ticket);
                if (ticket.getId() == -1) {  // Новый билет (пример)
                    ticket.setOwnerId(loginManager.getCurrentUser().getId());
                    if (ticket.validate()) {
                        try {
                            // отправка на сервер
                            Insert insert = new Insert(chandler);
                            long newId = insert.execute_gui(ticket);
                            ticket.setId(newId);
                            objectCanvas.addTicketAnimated(ticket);
                            ticketTable.refresh();
                            errorLabel.setText("Билет успешно добавлен");
                            ticketTable.setEditable(false);
                        } catch (Exception ex) {
                            errorLabel.setText("Ошибка при добавлении: " + ex.getMessage());
                        }
                    } else {
                        errorLabel.setText("Ошибка при добавлении: невалидный билет");
                    }
                } else {
                    if (ticket.validate()) {
                        try {
                            System.out.println(ticket);
                            UpdateId update = new UpdateId(chandler);
                            String message = update.execute_gui(ticket.getId(), ticket);
                            objectCanvas.addTicketAnimated(ticket);
                            ticketTable.refresh();
                            errorLabel.setText(message);
                        } catch (Exception ex) {
                            errorLabel.setText("Ошибка при обновлении: " + ex.getMessage());
                        }
                    } else {
                        errorLabel.setText("Ошибка при обновлении: невалидный билет");
                    }
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Ticket ticket = getTableView().getItems().get(getIndex());
                if (ticket.getId() == -1 || isEditMode ) {
                    setGraphic(confirmButton);
                } else {
                    setGraphic(null);
                }
            }
        }
    });
    cancelColumn.setCellFactory(col -> new TableCell<>() {
    private final Button cancelButton = new Button("✖️");

    {
        cancelButton.setOnAction(e -> {
            Ticket ticket = getTableView().getItems().get(getIndex());

            if (ticket.getId() == -1) { // Только для нового билета
                masterData.remove(ticket);
                ticketTable.refresh();
            }
        });

        cancelButton.setStyle("-fx-text-fill: red;");
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            Ticket ticket = getTableView().getItems().get(getIndex());
            if (ticket.getId() == -1 || isEditMode) {
                setGraphic(cancelButton);
            } else {
                setGraphic(null);
            }
        }
    }
});
        
    trashColumn.setCellFactory(col -> new TableCell<>() {
    private final Button trashButton = new Button("🗑️");
    {
        trashButton.setOnAction(e -> {
            Ticket ticket = getTableView().getItems().get(getIndex());

            if (ticket.getOwnerId() == loginManager.getCurrentUser().getId()) { 
                try {
                    // Только для нового билета
                    Remove remove = new Remove(chandler);
                    String msg = remove.execute_gui(ticket.getId());
                    objectCanvas.removeTicketAnimated(ticket);
                    masterData.remove(ticket);
                    showError(msg);
                    ticketTable.refresh();
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            } else {
                showError("У вас нет прав на удаление этого билета");
            }
        });

        trashButton.setStyle("-fx-text-fill: black;");
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            if (isTrashMode) {
                setGraphic(trashButton);
            } else {
                setGraphic(null);
            }
        }
    }
});
        


        // Название
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            String newName = event.getNewValue();
            if (newName == null || newName.trim().isEmpty()) {
                showError("Имя не может быть пустым.");
                ticketTable.refresh();
                return;
            }
            event.getRowValue().setName(newName.trim());
            
        });

        // X
        xColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        xColumn.setOnEditCommit(event -> {
            Float newX = event.getNewValue().floatValue();
            event.getRowValue().getCoordinates().setX(newX);
        });

        // Y
        yColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        yColumn.setOnEditCommit(event -> {
            Long newY = event.getNewValue().longValue();
            event.getRowValue().getCoordinates().setY(newY);
        });

        // Цена
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Integer price = event.getNewValue().intValue();
            if (price <= 0) {
                showError("Цена должна быть положительной.");
                ticketTable.refresh();
                return;
            }
            event.getRowValue().setPrice(price);
        });

        // Скидка
        discountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        discountColumn.setOnEditCommit(event -> {
            Double discount = event.getNewValue();
            if (discount < 0 || discount > 100) {
                showError("Скидка должна быть в диапазоне 0–100.");
                ticketTable.refresh();
                return;
            }
            event.getRowValue().setDiscount(discount);
        });

        refundableColumn.setCellFactory(ComboBoxTableCell.forTableColumn(true, false));
        refundableColumn.setOnEditCommit(event -> {
        Ticket ticket = event.getRowValue();
        try {
            ticket.setRefundable(event.getNewValue());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid value: " + event.getNewValue());
        }
        });
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn("VIP", "USUAL", "BUDGETARY"));
        typeColumn.setOnEditCommit(event -> {
        Ticket ticket = event.getRowValue();

        try {
            TicketType type = TicketType.valueOf(event.getNewValue());
            ticket.setType(type);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid enum value: " + event.getNewValue());
        }
        });
        

        // Название площадки
        venueNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        venueNameColumn.setOnEditCommit(event -> {
            String newName = event.getNewValue();
            if (newName == null || newName.trim().isEmpty()) {
                showError("Название площадки не может быть пустым.");
                ticketTable.refresh();
                return;
            }
            event.getRowValue().getVenue().setName(newName.trim());
        });       

        // Вместимость площадки
        venueCapacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        venueCapacityColumn.setOnEditCommit(event -> {
            int cap = event.getNewValue();
            if (cap <= 0) {
                showError("Вместимость должна быть положительной.");
                ticketTable.refresh();
                return;
            }
            event.getRowValue().getVenue().setCapacity(cap);
        });


        venueTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn("BAR", "THEATRE", "MALL"));
        venueTypeColumn.setOnEditCommit(event -> {
        Ticket ticket = event.getRowValue();

        try {
            VenueType type = VenueType.valueOf(event.getNewValue());
            ticket.getVenue().setType(type);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid enum value: " + event.getNewValue());
        }
        });

        // Список и фильтрация
        Show show = new Show(chandler);
        try {
            tickets = show.execute_gui();
        } catch (CommandException e){
            showError(e.getMessage());
        }

        masterData = FXCollections.observableArrayList(tickets);

        filteredData = new FilteredList<>(masterData, p -> true);
        SortedList<Ticket> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ticketTable.comparatorProperty());
        // ticketTable.getColumns().add(actionColumn);
        ticketTable.setItems(sortedData);

        // filteredData = new FilteredList<>(FXCollections.observableArrayList(tickets), p -> true);

        filterField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            filteredData.setPredicate(ticket ->
                    ticket.getName().toLowerCase().contains(lower) ||
                    String.valueOf(ticket.getOwnerId()).contains(lower)
            );
        });

        // SortedList<Ticket> sortedData = new SortedList<>(filteredData);
        // sortedData.comparatorProperty().bind(ticketTable.comparatorProperty());
        // ticketTable.setItems(sortedData);
        // ticketTable.setEditable(true);

        ticketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedTicket = newSel;
        });

        // Графика объектов
        // canvas = new ObjectCanvas();
        objectCanvas = new ObjectCanvas(canvas);
        objectCanvas.setTickets(tickets);
        objectCanvas.setOnTicketClicked(this::showTicketInfo);
        objectCanvas.drawObjects();

        // objectCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        //     Ticket clicked = objectCanvas.getTicketAt(e.getX(), e.getY());
        //     if (clicked != null) {
        //         showTicketInfo(clicked);
        //     }
        // });

        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());

        // Выход
        logoutButton.setOnAction(e -> {
            try {
                loginManager.logout();
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                toLoginScreen(stage);
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage()); 
            }
        });
    }

    private void handleAdd() {
        ticketTable.setEditable(true);
        // открыть модальное окно добавления (не реализовано в этом фрагменте)
        Ticket newTicket = new Ticket(); // или твой билдер с дефолтами
        newTicket.setName(""); // пустое имя, чтобы начать ввод
        newTicket.setId(-1);
        // и т.д. по всем полям, которые нужны

        ObservableList<Ticket> items = ticketTable.getItems();
        // items.add(newTicket);
        masterData.add(newTicket);

        ticketTable.scrollTo(newTicket);
        ticketTable.layout(); // обновить отрисовку

        // Начать редактирование первой ячейки новой строки
        int newIndex = items.indexOf(newTicket);
        ticketTable.getSelectionModel().select(newIndex);
        ticketTable.getFocusModel().focus(newIndex, nameColumn);
        ticketTable.edit(newIndex, nameColumn);
        errorLabel.setText("");
        // ticketTable.setEditable(false);
    }

    private void handleEdit() {
        if (isEditMode) {
            cancelEditMode();
        } else {
            handleEditMode();
        }
    }

    @FXML
    private void handleEditMode() {
        isEditMode = true;
        int currentUserId = loginManager.getCurrentUser().getId();

        // Сохраняем копию билетов пользователя (чтобы отменить при необходимости)
        originalUserTickets = masterData.stream()
            .filter(ticket -> ticket.getOwnerId() == currentUserId)
            .map(Ticket::clone) // Тебе нужно реализовать clone() у Ticket
            .toList();

        // Фильтруем таблицу: показываем только свои билеты
        filteredData.setPredicate(ticket -> ticket.getOwnerId() == currentUserId);

        ticketTable.setEditable(true);
        ticketTable.refresh();
    }

    private void cancelEditMode() {
        isEditMode = false;
        ticketTable.setEditable(false);

        // Повторно получаем билеты с сервера
        Show show = new Show(chandler);
        try {
            Collection<Ticket> updatedTickets = show.execute_gui();
            // System.out.println(updatedTickets);
            masterData.setAll(updatedTickets); // Обновляем данные
            filteredData.setPredicate(null);
            errorLabel.setText("Данные обновлены");
        } catch (CommandException e) {
            errorLabel.setText("Ошибка при обновлении данных: " + e.getMessage());
        }

        ticketTable.refresh();
    }

    private void handleDelete() {
        if (isTrashMode) {
            cancelTrashMode();
        } else {
            handleTrashMode();
        }
    }

    @FXML
    private void handleTrashMode() {
        isTrashMode = true;
        int currentUserId = loginManager.getCurrentUser().getId();

        // Сохраняем копию билетов пользователя (чтобы отменить при необходимости)
        originalUserTickets = masterData.stream()
            .filter(ticket -> ticket.getOwnerId() == currentUserId)
            .map(Ticket::clone) // Тебе нужно реализовать clone() у Ticket
            .toList();

        // Фильтруем таблицу: показываем только свои билеты
        filteredData.setPredicate(ticket -> ticket.getOwnerId() == currentUserId);
        ticketTable.refresh();
    }

    private void cancelTrashMode() {
        isTrashMode = false;
        // Повторно получаем билеты с сервера
        Show show = new Show(chandler);
        try {
            Collection<Ticket> updatedTickets = show.execute_gui();
            masterData.setAll(updatedTickets); // Обновляем данные
            filteredData.setPredicate(null);
            errorLabel.setText("Данные обновлены");
        } catch (CommandException e) {
            errorLabel.setText("Ошибка при обновлении данных: " + e.getMessage());
        }

        ticketTable.refresh();
    }

    private void showTicketInfo(Ticket ticket) {
        ticketInfoArea.setText(
            "ID: " + ticket.getId() + "\n" +
            "Название: " + ticket.getName() + "\n" +
            "Владелец: " + ticket.getOwnerId() + "\n" +
            "Координаты: (" + ticket.getCoordinates().getX() + ", " + ticket.getCoordinates().getY() + ")\n" +
            "Цена: " + ticket.getPrice() + "\n" +
            "Скидка: " + ticket.getDiscount() + "\n" +
            "Возврат: " + (ticket.getRefundable() ? "Да" : "Нет") + "\n" +
            "Тип: " + ticket.getType() + "\n" +
            "Место: " + (ticket.getVenue() != null ? ticket.getVenue().getName() : "—")
        );
    }

    private void toLoginScreen(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/views/login.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController controller = loader.getController();
        controller.init(chandler, loginManager);

        stage.setScene(scene);
        stage.setTitle("Ticket Manager - Авторизация");
        stage.show();
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
