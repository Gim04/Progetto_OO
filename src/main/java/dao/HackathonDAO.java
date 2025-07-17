package dao;

import model.Documento;
import model.Hackathon;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;

public interface HackathonDAO
{

    /*
        +-+-+-+-+-+-+-+-+-+
        |H|a|c|k|a|t|h|o|n|
        +-+-+-+-+-+-+-+-+-+
     */
    ArrayList<Hackathon> getHackathonList();

    ArrayList<Hackathon> getHackathonListForJudge(String emailGiudice);

    ArrayList<Hackathon> getHackathonListForOrganizzatore(String email);

    boolean inserisciHackathon(String nome, int dimensioneTeam, int maxIscritti, LocalDate dataI, LocalDate dataF, boolean registrazioni, String email);

    ArrayList<Documento> getDocumenti(String nome, String hackathon);

    boolean checkRegistrazioniChiuse(String hackathon);

    DefaultTableModel calculateClassifica(String hackathon);
}
