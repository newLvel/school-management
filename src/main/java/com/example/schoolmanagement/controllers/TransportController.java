package com.example.schoolmanagement.controllers;

import com.example.schoolmanagement.dao.TransportDAO;
import com.example.schoolmanagement.models.TransportRoute;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.Optional;

public class TransportController {

    @FXML private TableView<TransportRoute> routeTable;
    @FXML private TableColumn<TransportRoute, Integer> colId;
    @FXML private TableColumn<TransportRoute, String> colRouteName;
    @FXML private TableColumn<TransportRoute, String> colVehicle;
    @FXML private TableColumn<TransportRoute, String> colDriver;
    @FXML private TableColumn<TransportRoute, Double> colCost;
    @FXML private TableColumn<TransportRoute, Void> colActions;
    @FXML private TextField searchField;

    private final TransportDAO transportDAO = new TransportDAO();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadRoutes();
        searchField.textProperty().addListener((obs, old, val) -> {
            // Filter logic here if needed
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRouteName.setCellValueFactory(new PropertyValueFactory<>("routeName"));
        colVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colDriver.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        addActionsToTable();
    }

    private void loadRoutes() {
        routeTable.setItems(FXCollections.observableArrayList(transportDAO.getAllRoutes()));
    }

    private void addActionsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.getStyleClass().addAll("btn-primary", "action-button");
                deleteBtn.getStyleClass().addAll("danger-button", "action-button");
                editBtn.setOnAction(event -> showRouteDialog(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(event -> {
                    TransportRoute route = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Route");
                    alert.setHeaderText("Delete route " + route.getRouteName() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        transportDAO.deleteRoute(route.getId());
                        loadRoutes();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, editBtn, deleteBtn));
            }
        });
    }

    @FXML
    private void handleAddRoute() {
        showRouteDialog(null);
    }

    private void showRouteDialog(TransportRoute route) {
        Dialog<TransportRoute> dialog = new Dialog<>();
        dialog.setTitle(route == null ? "Add Transport Route" : "Edit Transport Route");
        dialog.setHeaderText(null);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField routeNameField = new TextField();
        TextField vehicleField = new TextField();
        TextField driverField = new TextField();
        TextField costField = new TextField();
        if (route != null) {
            routeNameField.setText(route.getRouteName());
            vehicleField.setText(route.getVehicleNumber());
            driverField.setText(route.getDriverName());
            costField.setText(String.valueOf(route.getCost()));
        }
        grid.add(new Label("Route Name:"), 0, 0);
        grid.add(routeNameField, 1, 0);
        grid.add(new Label("Vehicle Number:"), 0, 1);
        grid.add(vehicleField, 1, 1);
        grid.add(new Label("Driver Name:"), 0, 2);
        grid.add(driverField, 1, 2);
        grid.add(new Label("Cost:"), 0, 3);
        grid.add(costField, 1, 3);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == saveButtonType) {
                double cost = 0.0;
                try { cost = Double.parseDouble(costField.getText()); } catch (NumberFormatException ignored) {}
                if (route == null) {
                    return new TransportRoute(routeNameField.getText(), vehicleField.getText(), driverField.getText(), cost);
                } else {
                    route.setRouteName(routeNameField.getText());
                    route.setVehicleNumber(vehicleField.getText());
                    route.setDriverName(driverField.getText());
                    route.setCost(cost);
                    return route;
                }
            }
            return null;
        });
        Optional<TransportRoute> result = dialog.showAndWait();
        result.ifPresent(r -> {
            if (route == null) transportDAO.addRoute(r);
            else transportDAO.updateRoute(r);
            loadRoutes();
        });
    }
}
