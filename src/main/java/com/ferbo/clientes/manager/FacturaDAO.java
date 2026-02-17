package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.beans.MbLogin;
import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.Factura;
import com.ferbo.clientes.model.StatusFactura;
import com.ferbo.clientes.model.TipoFacturacion;

public class FacturaDAO extends DAO{
	
	private static Logger log = LogManager.getLogger(FacturaDAO.class);

    private static final String SELECT = "SELECT id, cliente, numero, moneda, rfc, nombre_cliente, fecha, observacion, subtotal, iva, total, pais, estado, municipio, ciudad, colonia, cp, calle, num_ext, num_int, telefono, fax, porcentaje_iva, numero_cliente, valor_declarado, inicio_servicios, fin_servicios, monto_letra, status, tipo_facturacion, planta, plazo, retencion, nom_serie, metodo_pago, tp_persona, cd_regimen, cd_uso_cfdi, uuid, emi_nombre, emi_rfc, emi_cd_regimen FROM factura ";
    private static final String INSERT = "INSERT INTO factura "
            + "(id, cliente, numero, moneda, rfc, nombre_cliente, fecha, observacion, subtotal, iva, total, pais, estado, municipio, ciudad, colonia, cp, calle, num_ext, num_int, telefono, fax, porcentaje_iva, numero_cliente, valor_declarado, inicio_servicios, fin_servicios, monto_letra, status, tipo_facturacion, planta, plazo, retencion, nom_serie, metodo_pago, tp_persona, cd_regimen, cd_uso_cfdi, uuid, emi_nombre, emi_rfc, emi_cd_regimen) "
            + "VALUES "
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE = "UPDATE factura SET "
            + "cliente=?, numero=?, moneda=?, rfc=?, nombre_cliente=?, fecha=?, observacion=?, subtotal=?, iva=?, total=?, pais=?, estado=?, municipio=?, ciudad=?, colonia=?, cp=?, calle=?, num_ext=?, num_int=?, telefono=?, fax=?, porcentaje_iva=?, numero_cliente=?, valor_declarado=?, inicio_servicios=?, fin_servicios=?, monto_letra=?, status=?, tipo_facturacion=?, planta=?, plazo=?, retencion=?, nom_serie=?, metodo_pago=?, tp_persona=?, cd_regimen=?, cd_uso_cfdi=?, uuid=?, emi_nombre=?, emi_rfc=?, emi_cd_regimen=? "
            + "WHERE cte_cve = ? ";
    
    private static final String SELECT_LOGIN = "SELECT * FROM cliente WHERE habilitado = 1 AND CTE_NOMBRE = ? AND CTE_RFC = ?";
    
    public static Factura getModel(ResultSet rs) throws SQLException {
    	
    	Factura f = new Factura();
    	
    	f.setId(rs.getInt("id"));
    	f.setIdCliente(rs.getInt("cliente"));
    	f.setNumero(rs.getString("numero"));
    	f.setMoneda(rs.getString("moneda"));
    	f.setRfc(rs.getString("rfc"));
    	f.setNombreCliente(rs.getString("nombre_cliente"));
    	f.setFecha(rs.getDate("fecha"));
    	f.setObservacion(rs.getString("observacion"));
    	f.setSubtotal(rs.getBigDecimal("subtotal"));
    	f.setIva(rs.getBigDecimal("iva"));
    	f.setTotal(rs.getBigDecimal("total"));
    	f.setPais(rs.getString("pais"));
    	f.setEstado(rs.getString("estado"));
    	f.setMunicipio(rs.getString("municipio"));
    	f.setCiudad(rs.getString("ciudad"));
    	f.setColonia(rs.getString("colonia"));
    	f.setCp(rs.getString("cp"));
    	f.setCalle(rs.getString("calle"));
    	f.setNumExt(rs.getString("num_ext"));
    	f.setNumInt(rs.getString("num_int"));
    	f.setTelefono(rs.getString("telefono"));
    	f.setFax(rs.getString("fax"));
    	f.setPorcentajeIva(rs.getBigDecimal("porcentaje_iva"));
    	f.setNumeroCliente(rs.getString("numero_cliente"));
    	f.setValorDeclarado(rs.getBigDecimal("valor_declarado"));
    	f.setInicioServicios(rs.getDate("inicio_servicios"));
    	f.setFinServicios(rs.getDate("fin_servicios"));
    	f.setMontoLetra(rs.getString("monto_letra"));
    	f.setIdStatus(rs.getInt("status"));
    	
    	f.setIdFacturacion(rs.getInt("tipo_facturacion"));
    	f.setIdPlanta(rs.getInt("planta"));
    	f.setPlazo(rs.getInt("plazo"));
    	f.setRetencion(rs.getBigDecimal("retencion"));
    	f.setNomSerie(rs.getString("nom_serie"));
    	f.setMetodoPago(rs.getString("metodo_pago"));
    	f.setTipoPersona(rs.getString("tp_persona"));
    	f.setCdRegimen(rs.getString("cd_regimen"));
    	f.setCdUsoCfdi(rs.getString("cd_uso_cfdi"));
    	f.setUuid(rs.getString("uuid"));
    	f.setEmisorNombre(rs.getString("emi_nombre"));
    	f.setEmisorRFC(rs.getString("emi_rfc"));
    	f.setEmisorCdRegimen(rs.getString("emi_cd_regimen"));
    	
    	return f;
    }
    
    public Factura get(Connection conn, Factura cliente) throws SQLException{
    	Cliente c = new Cliente();
        PreparedStatement psSelect = null;
        ResultSet rs = null;
		Factura f = new Factura();
		Planta p = new Planta();
		StatusFactura sf = new StatusFactura();
		String fSelect = null;
        ArrayList<Factura> listaFactura = null;

        
    	try {
    		fSelect = SELECT + "WHERE cliente = ? ";
    		psSelect = conn.prepareStatement(fSelect);
    		log.debug("Preparando sentencia SELECT para la tabla Factura...");
    		log.debug(fSelect);
    		log.trace("id", + cliente.getCliente().getIdCliente());
    		if(rs.next() ) {
    			c = new Cliente();
    			p = new Planta();
    			f = new Factura();
    			sf = new StatusFactura();
    			getModel(rs);
    			listaFactura.add(f);
    		}
    		log.debug("La sentencia Select para la tabla Factura termino satisfactoriamente");
    		
    	}finally{
    		close(rs);
    		close(psSelect);
    	}
    	return f;
    	
    }
    
    public List<Factura> buscarFacturas(Connection conn, Date fecha_ini, Date fecha_fin) throws SQLException {
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        String sql = null;
        ArrayList<Factura> listaFacturas= null;
        Factura factura= null;
        int idx = 1;
   	try {
   		
			sql = SELECT + "WHERE fecha  BETWEEN ? AND ?" ;
			log.debug("Procesando lista de facturas");
			
			psSelect =  conn.prepareStatement(sql);
			psSelect.setDate(idx++, getSqlDate(fecha_ini));
			psSelect.setDate(idx++, getSqlDate(fecha_fin));
			rsSelect = psSelect.executeQuery();
			
			listaFacturas = new ArrayList<>();
			
			while(rsSelect.next()) {
				factura = getModel(rsSelect);
				listaFacturas.add(factura);
			}
			
			log.debug("Lista procesada...");
		}finally {
			close(rsSelect);
			close(psSelect);
		}
   	
		return listaFacturas;
   	
   }
    	
    
    
    public List<Factura> buscarFacturasPorCliente(Connection conn, Date fecha_ini, Date fecha_fin, Integer clienteCve) throws SQLException{
    	
         PreparedStatement psSelect = null;
         ResultSet rsSelect = null;
         String sql = null;
         ArrayList<Factura> listaFacturas= null;
         Factura factura= null;
         int idx = 1;
    	try {
    		
			sql = SELECT + "WHERE fecha  BETWEEN ? AND ? AND cliente  = ?  " ;
			
			psSelect =  conn.prepareStatement(sql);
			psSelect.setDate(idx++, getSqlDate(fecha_ini));
			psSelect.setDate(idx++, getSqlDate(fecha_fin));
			psSelect.setInt(idx++, clienteCve);
			log.debug("Procesando lista de facturas");
			
			rsSelect = psSelect.executeQuery();
			
			listaFacturas = new ArrayList<>();
			
			while(rsSelect.next()) {
				factura = getModel(rsSelect);
				listaFacturas.add(factura);
			}
			
			log.debug("Lista procesada...");
		}finally {
			close(rsSelect);
			close(psSelect);
		}
    	
		return listaFacturas;
    	
    }
    
}
