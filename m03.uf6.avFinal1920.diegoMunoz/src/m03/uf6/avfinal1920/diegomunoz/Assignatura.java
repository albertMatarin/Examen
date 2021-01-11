/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m03.uf6.avfinal1920.diegomunoz;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jmartin
 */
public class Assignatura {
  
  private IntegerProperty codi;
  private StringProperty nom;
  private IntegerProperty credits;
  
  public Assignatura(int codi, String nom, int credits){
    this.codi = new SimpleIntegerProperty(codi);
    this.nom = new SimpleStringProperty(nom);
    this.credits = new SimpleIntegerProperty(credits);
  }

  public IntegerProperty getCodiProperty() {
    return codi;
  }
  
  public int getCodi(){
    return codi.get();
  }
  
  public void setCodi(int codi){
    this.codi.set(codi);
  }

  public StringProperty getNomProperty() {
    return nom;
  }
  
  public String getNom(){
    return nom.get();
  }
  
  public void setNom(String nom){
    this.nom.set(nom);
  }

  public IntegerProperty getCreditsProperty() {
    return credits;
  }
  
  public int getCredits(){
    return credits.get();
  }
  
  public void setCredits(int credits){
    this.credits.set(credits);
  }
}
