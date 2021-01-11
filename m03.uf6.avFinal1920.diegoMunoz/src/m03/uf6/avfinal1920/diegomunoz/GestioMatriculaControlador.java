/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m03.uf6.avfinal1920.diegomunoz;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jmartin
 */
public class GestioMatriculaControlador implements Initializable {

  @FXML
  private TextField tfNIF;
  @FXML
  private Label lblDadesAlumne;
  @FXML
  private TableView<Assignatura> tblDisponibles;
  @FXML
  private TableColumn<Assignatura, Integer> tcDispCodi;
  @FXML
  private TableColumn<Assignatura, String> tcDispNom;
  @FXML
  private TableColumn<Assignatura, Integer> tcDispCredits;
  @FXML
  private TableView<Assignatura> tblMatriculades;
  @FXML
  private TableColumn<Assignatura, Integer> tcMatCodi;
  @FXML
  private TableColumn<Assignatura, String> tcMatNom;
  @FXML
  private TableColumn<Assignatura, Integer> tcMatCredits;
  @FXML
  private Label lblMissatges;

  private Connection conn = null;

  private Map<Integer, Assignatura> m_asignaturasMap;
  private List<Assignatura> m_assignaturasNuevas;
  private ObservableList<Assignatura> m_asignaturas;
  private ObservableList<Assignatura> m_matriculadas;

  private Alumne m_currentAlumne;
  private int m_currentCreditCount;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    String cns = "SELECT * FROM assignatures";
    m_asignaturas = FXCollections.observableArrayList();
    m_matriculadas = FXCollections.observableArrayList();
    m_asignaturasMap = new HashMap<>();
    m_assignaturasNuevas = new ArrayList<>();

    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/matricula?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
        "admin_matricula",
        "admin");
      PreparedStatement ps = conn.prepareStatement(cns);
      ResultSet rs = ps.executeQuery();

      //inicializamos las asignaturas disponibles
      while (rs.next()) {
        m_asignaturas.add(new Assignatura(rs.getInt("codi"), rs.getString("nom"), rs.getInt("credits")));
      }

    } catch (SQLException e) {
    }

    //guardamos las asignaturas disponibles en un map para busquedas rapidas
    for (int i = 0; i < m_asignaturas.size(); i++) {
      m_asignaturasMap.put(m_asignaturas.get(i).getCodi(), m_asignaturas.get(i));
    }

    tcDispCodi.setCellValueFactory(dc -> dc.getValue().getCodiProperty().asObject());
    tcDispNom.setCellValueFactory(dc -> dc.getValue().getNomProperty());
    tcDispCredits.setCellValueFactory(dc -> dc.getValue().getCreditsProperty().asObject());

    tblDisponibles.setItems(m_asignaturas);

    tcMatCodi.setCellValueFactory(dc -> dc.getValue().getCodiProperty().asObject());
    tcMatNom.setCellValueFactory(dc -> dc.getValue().getNomProperty());
    tcMatCredits.setCellValueFactory(dc -> dc.getValue().getCreditsProperty().asObject());

    tblMatriculades.setItems(m_matriculadas);

    lblDadesAlumne.setText("");
    lblMissatges.setText("");

  }

  @FXML
  private void handleCercar(ActionEvent event) {
    m_matriculadas.clear();
    m_currentCreditCount = 0;

    String sntAlumne = "SELECT * FROM alumnes WHERE ? = alumnes.nif";
    String sntMatriculat = "SELECT * FROM matriculacions WHERE ? = matriculacions.nif_alumne";
    try {
      PreparedStatement ps = conn.prepareStatement(sntAlumne);
      ps.setString(1, tfNIF.getText().toString());
      ResultSet rs = ps.executeQuery();
      rs.next();

      //guardamos el alumno seleccionado
      m_currentAlumne = new Alumne(rs.getString("nif"), rs.getString("nom"), rs.getString("cognoms"));
      ps.close();

      lblDadesAlumne.setText(m_currentAlumne.getNom() + " " + m_currentAlumne.getCognoms());
      lblMissatges.setText("");

      ps = conn.prepareStatement(sntMatriculat);
      ps.setString(1, m_currentAlumne.getNif());
      rs = ps.executeQuery();

      //obtenemos las asignaturas matriculadas del alumno
      while (rs.next()) {
        m_matriculadas.add(m_asignaturasMap.get(rs.getInt("codi_assignatura")));
        m_currentCreditCount += m_matriculadas.get(m_matriculadas.size() - 1).getCredits();
      }
      ps.close();

    } catch (SQLException e) {
      lblDadesAlumne.setText("");
      lblMissatges.setText("L'alumne no existeix");
      System.out.println(e.getMessage());
    }
  }

  @FXML
  private void handleAfegir(ActionEvent event) {
    int selected = tblDisponibles.getSelectionModel().getSelectedIndex();
    int i = 0;
    Assignatura aux;
    boolean duplicated = false;

    lblMissatges.setText("");

    if (m_currentAlumne != null) { //si hay un alumno seleccionado
      if (selected != -1) { //si hay una asignatura a matricular seleccionada
        aux = m_asignaturas.get(selected);
        if (m_currentCreditCount + aux.getCredits() <= 40) { //si no nos pasamos de los creditos con la nueva asignatura
          while (!duplicated && i < m_matriculadas.size()) {
            duplicated = aux.getCodi() == m_matriculadas.get(i).getCodi();
            i++;
          }
          if (!duplicated) { //si la asignatura no esta duplicada
            m_currentCreditCount += aux.getCredits();
            m_matriculadas.add(aux);
            m_assignaturasNuevas.add(aux);

          } else {
            lblMissatges.setText("L'alumne ja esta matriculat a aquesta assignatura");
          }
        } else {
          lblMissatges.setText("La suma de credits seria superior a 40");
        }
      } else {
        lblMissatges.setText("No has seleccionat cap assignatura");
      }
    } else {
      lblMissatges.setText("Selecciona primer un alumne");
    }

  }

  @FXML
  private void handleMatricular(ActionEvent event) {
    String snt = "{ CALL matricula(?,?)}";
    CallableStatement cs = null;
    lblMissatges.setText("");

    if (m_currentAlumne != null) { //si hay un alumno seleccionado
      if (m_assignaturasNuevas.size() > 0) { //si ha habido algun cambio
        try {
          //iteramos anadiendo las asignaturas nuevas matriculadas en la BBDD
          for (int i = 0; i < m_assignaturasNuevas.size(); i++) {
            cs = conn.prepareCall(snt);
            cs.setInt(1, m_assignaturasNuevas.get(i).getCodi());
            cs.setString(2, m_currentAlumne.getNif());

            cs.execute();
            cs.close();
          }
          lblMissatges.setText("Alumne matriculat correctament");

        } catch (SQLException e) {
          lblMissatges.setText("Error al matricular l'alumne");
          System.out.println(e.getMessage());
        }

        m_matriculadas.clear();
        m_assignaturasNuevas.clear();
        m_currentAlumne = null;
        m_currentCreditCount = 0;
        
        tfNIF.clear();
        lblDadesAlumne.setText("");
      } else {
        lblMissatges.setText("No hi ha cap assignatura nova a matricular");
      }

    } else {
      lblMissatges.setText("Selecciona primer un alumne");
    }
  }

}
