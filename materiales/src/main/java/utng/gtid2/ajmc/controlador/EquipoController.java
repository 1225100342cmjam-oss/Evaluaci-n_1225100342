package utng.gtid2.ajmc.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid2.ajmc.dao.EquipoDAO;
import utng.gtid2.ajmc.modelo.Equipo;
import utng.gtid2.ajmc.modelo.Responsable;
import utng.gtid2.ajmc.modelo.TipoEquipo;

import java.sql.Timestamp;
import java.util.Optional;

public class EquipoController {

    @FXML
    private TextField txtCodigo, txtNombre;

    // Renombrado de cbTipo -> cmbTipo para coincidir con el fx:id del FXML
    @FXML
    private ComboBox<TipoEquipo> cmbTipo;

    @FXML
    private ComboBox<Responsable> cbResponsable;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private TextArea txtDescripcion;

    // --- Panel "Registrar baja" ---
    @FXML
    private TitledPane panelBaja;

    @FXML
    private ComboBox<String> cbMotivoBaja;

    @FXML
    private TextArea txtObservacionesBaja;

    // --- Búsqueda ---
    @FXML
    private TextField txtBuscar;

    // --- Tabla ---
    @FXML
    private TableView<Equipo> tblEquipos;

    @FXML
    private TableColumn<Equipo, Integer> colId;

    @FXML
    private TableColumn<Equipo, String> colCodigo, colNombre, colEstado;

    @FXML
    private TableColumn<Equipo, Timestamp> colFechaRegistro;

    @FXML
    private TableColumn<Equipo, TipoEquipo> colTipo;

    @FXML
    private TableColumn<Equipo, Responsable> colResponsable;

    @FXML
    private Label lblEquipoSeleccionado;

    private EquipoDAO dao = new EquipoDAO();

    private ObservableList<Equipo> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        // Supone que Equipo tiene el getter getFechaRegistro(); ajusta el nombre
        // de la propiedad si en tu modelo se llama distinto.
        colFechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        try {
            cmbTipo.setItems(FXCollections.observableArrayList(dao.listarTipos()));
            cbResponsable.setItems(FXCollections.observableArrayList(dao.listarResponsables()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        cbEstado.setItems(FXCollections.observableArrayList(
                "Activo",
                "En reparación",
                "En resguardo",
                "De baja"
        ));

        cbMotivoBaja.setItems(FXCollections.observableArrayList(
                "Obsolescencia",
                "Daño irreparable",
                "Robo/Extravío",
                "Venta",
                "Donación",
                "Otro"
        ));

        panelBaja.setExpanded(false);

        cargarTabla();

        tblEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            if (newSelection != null) {
                txtCodigo.setText(newSelection.getCodigo());
                txtNombre.setText(newSelection.getNombre());
                cmbTipo.setValue(newSelection.getTipo());
                cbResponsable.setValue(newSelection.getResponsable());
                cbEstado.setValue(newSelection.getEstado());
                txtDescripcion.setText(newSelection.getDescripcion());

                lblEquipoSeleccionado.setText(
                        "Seleccionado: " + newSelection.getCodigo() + " - " + newSelection.getNombre());
            } else {
                lblEquipoSeleccionado.setText("Ningún equipo seleccionado");
            }

        });

    }

    @FXML
    public void guardar() {

        if (txtCodigo.getText().isEmpty()
                || txtNombre.getText().isEmpty()
                || cmbTipo.getValue() == null
                || cbEstado.getValue() == null) {

            mostrarAlerta("Error", "Completa todos los campos obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        try {

            Equipo equipo = new Equipo(
                    0,
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    cmbTipo.getValue(),
                    cbResponsable.getValue(),
                    cbEstado.getValue(),
                    txtDescripcion.getText(),
                    null
            );

            dao.insertar(equipo);

            cargarTabla();
            limpiarFormulario();

        } catch (Exception e) {

            e.printStackTrace();
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);

        }

    }

    @FXML
    public void modificar() {

        Equipo seleccionado = tblEquipos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            mostrarAlerta("Aviso", "Selecciona un equipo.", Alert.AlertType.WARNING);
            return;

        }

        seleccionado.setCodigo(txtCodigo.getText());
        seleccionado.setNombre(txtNombre.getText());
        seleccionado.setTipo(cmbTipo.getValue());
        seleccionado.setResponsable(cbResponsable.getValue());
        seleccionado.setEstado(cbEstado.getValue());
        seleccionado.setDescripcion(txtDescripcion.getText());

        try {

            dao.actualizar(seleccionado);

            cargarTabla();
            limpiarFormulario();

        } catch (Exception e) {

            e.printStackTrace();
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);

        }

    }

    @FXML
    public void eliminar() {

        Equipo seleccionado = tblEquipos.getSelectionModel().getSelectedItem();

        if (seleccionado == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Deseas eliminar este equipo?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {

                dao.eliminar(seleccionado.getIdEquipo());

                cargarTabla();
                limpiarFormulario();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    /**
     * Muestra/expande el panel de "Registrar baja" para el equipo seleccionado.
     */
    @FXML
    public void mostrarPanelBaja() {

        Equipo seleccionado = tblEquipos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un equipo para dar de baja.", Alert.AlertType.WARNING);
            return;
        }

        panelBaja.setExpanded(true);

    }

    /**
     * Confirma la baja del equipo seleccionado: actualiza su estado a "Baja"
     * y registra el motivo/observaciones en la tabla bajas_equipo mediante
     * dao.registrarBaja(...).
     */
    @FXML
    public void confirmar() {

        Equipo seleccionado = tblEquipos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un equipo.", Alert.AlertType.WARNING);
            return;
        }

        if (cbMotivoBaja.getValue() == null) {
            mostrarAlerta("Error", "Selecciona un motivo de baja.", Alert.AlertType.ERROR);
            return;
        }

        try {

            seleccionado.setEstado("De baja");
            dao.actualizar(seleccionado);

            String motivo = cbMotivoBaja.getValue();
            String observaciones = txtObservacionesBaja.getText();

            dao.registrarBaja(seleccionado.getIdEquipo(), motivo, observaciones);

            cargarTabla();
            limpiarFormulario();

            panelBaja.setExpanded(false);
            cbMotivoBaja.setValue(null);
            txtObservacionesBaja.clear();

        } catch (Exception e) {

            e.printStackTrace();
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);

        }

    }

    /**
     * Filtra la tabla por código o nombre según el texto de búsqueda.
     * Si txtBuscar está vacío, recarga la lista completa.
     */
    @FXML
    public void buscarEquipo() {

        String texto = txtBuscar.getText();

        if (texto == null || texto.isBlank()) {
            cargarTabla();
            return;
        }

        try {

            ObservableList<Equipo> filtrada = FXCollections.observableArrayList(dao.buscar(texto.trim()));
            tblEquipos.setItems(filtrada);

        } catch (Exception e) {

            e.printStackTrace();
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);

        }

    }

    private void cargarTabla() {

        try {

            lista.setAll(dao.listar());
            tblEquipos.setItems(lista);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * Limpia el formulario. Se expone como @FXML porque el botón "Limpiar"
     * del FXML llama a #limpiarFormulario directamente.
     */
    @FXML
    public void limpiarFormulario() {

        txtCodigo.clear();
        txtNombre.clear();
        cmbTipo.setValue(null);
        cbResponsable.setValue(null);
        cbEstado.setValue(null);
        txtDescripcion.clear();
        txtBuscar.clear();

        tblEquipos.getSelectionModel().clearSelection();
        lblEquipoSeleccionado.setText("Ningún equipo seleccionado");

        cargarTabla();

    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();

    }

}