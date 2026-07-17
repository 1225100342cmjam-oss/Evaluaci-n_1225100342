package utng.gtid2.ajmc.dao;

import utng.gtid2.ajmc.conexion.ConexionBD;
import utng.gtid2.ajmc.modelo.*;
import java.sql.*;
import java.util.*;

public class EquipoDAO {

    private Connection con;

     public EquipoDAO() {
        con = ConexionBD.getInstancia().getConexion();
    }

    // LISTAR
    public List<Equipo> listar() throws SQLException{
        List<Equipo> lista=new ArrayList<>();
        String sql="SELECT e.*, t.id_tipo,t.nombre tipo,t.descripcion desc_tipo,"
                +" r.id_responsable,r.nombre_completo,r.departamento,r.email,r.telefono "
                +"FROM equipos e "
                +"JOIN tipos_equipo t ON e.id_tipo=t.id_tipo "
                +"LEFT JOIN responsables r ON e.id_responsable=r.id_responsable "
                +"ORDER BY e.id_equipo";
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(sql);
        while(rs.next()){
            TipoEquipo tipo=new TipoEquipo(rs.getInt("id_tipo"),rs.getString("tipo"),rs.getString("desc_tipo"));
            Responsable resp=null;
            if(rs.getObject("id_responsable")!=null){
                resp=new Responsable(rs.getInt("id_responsable"),rs.getString("nombre_completo"),
                rs.getString("departamento"),rs.getString("email"),rs.getString("telefono"));
            }
            lista.add(new Equipo(rs.getInt("id_equipo"),rs.getString("codigo"),rs.getString("nombre"),
            tipo,resp,rs.getString("estado"),rs.getString("descripcion"),rs.getTimestamp("fecha_registro")));
        }
        rs.close();st.close();
        return lista;
    }

    public List<TipoEquipo> listarTipos() throws SQLException{
        List<TipoEquipo> l=new ArrayList<>();
        ResultSet rs=con.createStatement().executeQuery("SELECT * FROM tipos_equipo ORDER BY nombre");
        while(rs.next()) l.add(new TipoEquipo(rs.getInt(1),rs.getString(2),rs.getString(3)));
        return l;
    }

    public List<Responsable> listarResponsables() throws SQLException{
        List<Responsable> l=new ArrayList<>();
        ResultSet rs=con.createStatement().executeQuery("SELECT * FROM responsables ORDER BY nombre_completo");
        while(rs.next()) l.add(new Responsable(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
        return l;
    }

    public void insertar(Equipo e) throws SQLException{
        String sql="INSERT INTO equipos(codigo,nombre,id_tipo,id_responsable,estado,descripcion) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,e.getCodigo());
        ps.setString(2,e.getNombre());
        ps.setInt(3,e.getTipo().getIdTipo());
        if(e.getResponsable()!=null) ps.setInt(4,e.getResponsable().getIdResponsable()); else ps.setNull(4,Types.INTEGER);
        ps.setString(5,e.getEstado());
        ps.setString(6,e.getDescripcion());
        ps.executeUpdate();
    }

    public void actualizar(Equipo e) throws SQLException{
        String sql="UPDATE equipos SET codigo=?,nombre=?,id_tipo=?,id_responsable=?,estado=?,descripcion=? WHERE id_equipo=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,e.getCodigo());
        ps.setString(2,e.getNombre());
        ps.setInt(3,e.getTipo().getIdTipo());
        if(e.getResponsable()!=null) ps.setInt(4,e.getResponsable().getIdResponsable()); else ps.setNull(4,Types.INTEGER);
        ps.setString(5,e.getEstado());
        ps.setString(6,e.getDescripcion());
        ps.setInt(7,e.getIdEquipo());
        ps.executeUpdate();
    }

    public List<Equipo> buscar(String texto) throws SQLException{
        List<Equipo> lista=new ArrayList<>();
        String sql="SELECT e.*,t.id_tipo,t.nombre tipo,t.descripcion desc_tipo,r.id_responsable,r.nombre_completo,r.departamento,r.email,r.telefono FROM equipos e JOIN tipos_equipo t ON e.id_tipo=t.id_tipo LEFT JOIN responsables r ON e.id_responsable=r.id_responsable WHERE UPPER(e.codigo) LIKE UPPER(?) OR UPPER(e.nombre) LIKE UPPER(?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,"%"+texto+"%");
        ps.setString(2,"%"+texto+"%");
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            TipoEquipo t=new TipoEquipo(rs.getInt("id_tipo"),rs.getString("tipo"),rs.getString("desc_tipo"));
            Responsable r=rs.getObject("id_responsable")==null?null:new Responsable(rs.getInt("id_responsable"),rs.getString("nombre_completo"),rs.getString("departamento"),rs.getString("email"),rs.getString("telefono"));
            lista.add(new Equipo(rs.getInt("id_equipo"),rs.getString("codigo"),rs.getString("nombre"),t,r,rs.getString("estado"),rs.getString("descripcion"),rs.getTimestamp("fecha_registro")));
        }
        return lista;
    }

    public void registrarBaja(int idEquipo,String motivo,String observaciones) throws SQLException{
        PreparedStatement ps=con.prepareStatement("INSERT INTO bajas_equipo(id_equipo,motivo,observaciones) VALUES(?,?,?)");
        ps.setInt(1,idEquipo); ps.setString(2,motivo); ps.setString(3,observaciones); ps.executeUpdate();
    }

    public void eliminar(int idEquipo) throws SQLException{
        PreparedStatement ps=con.prepareStatement("DELETE FROM equipos WHERE id_equipo=?");
        ps.setInt(1,idEquipo);
        ps.executeUpdate();
    }
}
