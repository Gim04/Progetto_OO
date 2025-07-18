package dao;

import model.Documento;
import model.Hackathon;
import model.Sede;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaccia per la gestione dei dati relativi agli hackathon.
 */
public interface HackathonDAO
{

    /*
        +-+-+-+-+-+-+-+-+-+
        |H|a|c|k|a|t|h|o|n|
        +-+-+-+-+-+-+-+-+-+
     */

    /**
     * Restituisce la lista di tutti gli hackathon presenti nel sistema.
     *
     * @return Lista di oggetti {@link Hackathon}.
     */
    List<Hackathon> getHackathonList();

    /**
     * Restituisce la lista degli hackathon a cui è associato un giudice specifico.
     *
     * @param emailGiudice L'indirizzo email del giudice.
     * @return Lista di hackathon associati al giudice.
     */
    List<Hackathon> getHackathonListForJudge(String emailGiudice);

    /**
     * Restituisce la lista degli hackathon creati o gestiti da un organizzatore specifico.
     *
     * @param email L'indirizzo email dell'organizzatore.
     * @return Lista di hackathon dell'organizzatore.
     */
    List<Hackathon> getHackathonListForOrganizzatore(String email);

    /**
     * Inserisce un nuovo hackathon nel sistema.
     *
     * @param nome           Il nome dell'hackathon.
     * @param dimensioneTeam Il numero massimo di membri per team.
     * @param maxIscritti    Il numero massimo di iscritti totali.
     * @param dataI          La data di inizio dell'hackathon.
     * @param dataF          La data di fine dell'hackathon.
     * @param registrazioni  True se le registrazioni sono aperte, false se chiuse.
     * @param email          L'email dell'organizzatore.
     * @param sede           La sede fisica in cui si svolge l'hackathon.
     * @return True se l'inserimento è avvenuto con successo, false altrimenti.
     */
    boolean inserisciHackathon(String nome, int dimensioneTeam, int maxIscritti, LocalDate dataI, LocalDate dataF, boolean registrazioni, String email, Sede sede);

    /**
     * Restituisce i documenti associati a un team partecipante a un determinato hackathon.
     *
     * @param nome      Il nome del team.
     * @param hackathon Il nome dell'hackathon.
     * @return Lista di oggetti {@link Documento} associati al team e all'hackathon specificati.
     */
    List<Documento> getDocumenti(String nome, String hackathon);

    /**
     * Verifica se le registrazioni per un dato hackathon sono chiuse.
     *
     * @param hackathon Il nome dell'hackathon.
     * @return True se le registrazioni sono chiuse, false se ancora aperte.
     */
    boolean checkRegistrazioniChiuse(String hackathon);

    /**
     * Calcola e restituisce la classifica dei team per un determinato hackathon.
     *
     * @param hackathon Il nome dell'hackathon.
     * @return Un {@link DefaultTableModel} contenente la classifica.
     */
    DefaultTableModel calculateClassifica(String hackathon);

    /**
     * Restituisce la lista dei nomi delle sedi disponibili per lo svolgimento degli hackathon.
     *
     * @return Lista di nomi di sedi.
     */
    List<String> getSedi();
}
