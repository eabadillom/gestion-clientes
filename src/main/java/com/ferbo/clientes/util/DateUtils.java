package com.ferbo.clientes.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

/**Esta clase agrupa varios metodos para la operacion con fechas.
 * Los dos métodos principales son getFechaLiquidacion y esDiaAsueto,
 * ambos consultan a un archivo DateUtils.properties o directamente
 * a la Base de Datos de Scrittura cuáles son los días de asueto que
 * se han declarado previamente.
 * @author Esteban Antonio Badillo Martinez
 * @version 1.0
 */
public class DateUtils extends LoadProperties {

	public static String PREF_FECHA        = "fecha"; //Para lectura del .properties
	public static String PREF_ASUETO       = "asueto"; //Para lectura del .properties
	public static String PREF_DIA_ESPECIAL = "especial"; //Para lectura del .properties
	
	public static final String FORMATO_DD_MM_YYYY          = "dd/MM/yyyy";
	public static final String FORMATO_YYYY_MM_DD          = "yyyy-MM-dd";
	public static final String FORMATO_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss.SSS";
	public static final String FORMATO_DD_MM_YYYY_FULL     = "dd MMMM yyyy";
	
	public static final int ENERO      = Calendar.JANUARY;
	public static final int FEBRERO    = Calendar.FEBRUARY;
	public static final int MARZO      = Calendar.MARCH;
	public static final int ABRIL      = Calendar.APRIL;
	public static final int MAYO       = Calendar.MAY;
	public static final int JUNIO      = Calendar.JUNE;
	public static final int JULIO      = Calendar.JULY;
	public static final int AGOSTO     = Calendar.AUGUST;
	public static final int SEPTIEMBRE = Calendar.SEPTEMBER;
	public static final int OCTUBRE    = Calendar.OCTOBER;
	public static final int NOVIEMBRE  = Calendar.NOVEMBER;
	public static final int DICIEMBRE  = Calendar.DECEMBER;
	
	public static final int AM         = Calendar.AM;
	public static final int PM         = Calendar.PM;
	
	public static String PROP_ENERO      = "enero"; //Para lectura del .properties
	public static String PROP_FEBRERO    = "febrero"; //Para lectura del .properties
	public static String PROP_MARZO      = "marzo"; //Para lectura del .properties
	public static String PROP_ABRIL      = "abril"; //Para lectura del .properties
	public static String PROP_MAYO       = "mayo"; //Para lectura del .properties
	public static String PROP_JUNIO      = "junio"; //Para lectura del .properties
	public static String PROP_JULIO      = "julio"; //Para lectura del .properties
	public static String PROP_AGOSTO     = "agosto"; //Para lectura del .properties
	public static String PROP_SEPTIEMBRE = "septiembre"; //Para lectura del .properties
	public static String PROP_OCTUBRE    = "octubre"; //Para lectura del .properties
	public static String PROP_NOVIEMBRE  = "noviembre"; //Para lectura del .properties
	public static String PROP_DICIEMBRE  = "diciembre"; //Para lectura del .properties
	
	public static final String KEY_SEPARADOR_DIA = " ";//Para lectura del .properties
	
	//Para lectura de las fechas de asueto en la Base de Datos.
	//Las fechas de asueto se pueden categorizar incluso hasta por País
	//(o región geográfica, según la tabla IPITOOLS_HOLYDAYS O BMEA_IPITOOLS_HOLYDAYS
	//de la base de datos de Scrittura.
	public static final String UBICACION_MEXICO = "MX";
	
	public static final int LUNES = Calendar.MONDAY;
	public static final int MARTES = Calendar.TUESDAY;
	public static final int MIERCOLES = Calendar.WEDNESDAY;
	public static final int JUEVES = Calendar.THURSDAY;
	public static final int VIENRES = Calendar.FRIDAY;
	public static final int SABADO = Calendar.SATURDAY;
	public static final int DOMINGO = Calendar.SUNDAY;
	
	/**Metodo para agregar dias a una fecha dada.
	 * @param fecha Fecha de referencia a la que se desea agregar dias.
	 * @param dias Numero de dias a agregar.
	 * @return Nuevo objeto fecha con los dias agregados.
	 */
	public static Date addDay(Date fecha, int dias){
		Date resultado = null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.DATE, dias);
		
		resultado = new Date(c.getTimeInMillis()); 
		
		return resultado;
	}
	
	/**Metodo para agregar meses a una fecha dada.
	 * @param fecha Fecha de referencia a la que se desea agregar meses.
	 * @param meses Numero de meses a agregar.
	 * @return Nuevo objeto fecha con los meses agregados.
	 */
	public static Date addMonth(Date fecha, int meses){
		Date resultado = null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.MONTH, meses);
		
		resultado = new Date(c.getTimeInMillis());
		
		return resultado;
	}
	
	/**Metodo para agregar años a una fecha dada.
	 * @param fecha Fecha de referencia a la que se desea agregar años.
	 * @param anios Numero de años a agregar.
	 * @return Nuevo objeto fecha con los años agregados.
	 */
	public static Date addYear(Date fecha, int anios){
		Date resultado = null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.YEAR, anios);
		
		resultado = new Date(c.getTimeInMillis());
		
		return resultado;
	}
	
	/**Metodo para obtener el calculo de la fecha de liquidación. Este método considera
	 * el cálculo sin tomar en cuenta los días de fin de semana (Sábado y Domingo) y sin
	 * considerar días de asueto. Estos últimos se pueden leer desde un archivo
	 * DateUtils.properties, o bien, desde una tabla en la base de datos de Scrittura.
	 * @param fecha Fecha a partir de la cual se caculará la de Liquidación.
	 * @param dias Dias a partir de la fecha de liquidacion para obtener el vencimiento
	 * @return {@link Date} Fecha de Vencimiento.
	 * @throws DateUtilsException Cuando ocurre un problema con el calculo de la fecha
	 * de liquidación.
	 */
	public static Date fechaLiquidacion(Date fecha, int dias)
	throws DateUtilsException {
		return DateUtils.fechaLiquidacion(fecha, dias, false);
	}
	
	/**Método para obtener el cálculo de la fecha de liquidación. Este método considera
	 * el cálculo sin tomar los días de asueto. Se puede decidir a voluntad si se desea
	 * considerar los días de fin de semana (Sábado y domingo), se puede habilitar el
	 * parámetro quitarFinDeSemana en True. Si se desea quitar de consideración los días
	 * de fin de semana del cálculo, simplemente se deberá establecer el parámetro
	 * quitarFinDeSemana en False.
	 * @param fecha Fecha a partir de la cual se calculará la Fecha de Liquidación.
	 * @param dias
	 * @param quitarFinDeSemana
	 * @return Objeto {@link Date} con la Fecha de liquidación.
	 * @throws DateUtilsException En caso de error o problema con el cálculo de la fecha
	 * de liquidación.
	 */
	public static Date fechaLiquidacion(Date fecha, int dias, boolean quitarFinDeSemana)
	throws DateUtilsException{
		return DateUtils.fechaLiquidacion(fecha, dias, quitarFinDeSemana, false); 
	}
	
	/**Método para obtener el cálculo de la fecha de liquidación. Este método puede
	 * considerar a voluntad el considerar o no los días de fin de semana, así como los
	 * días de asueto.<br>
	 * Si se desea tomar en cuenta los días de fin de semana, se deberá establecer el
	 * parámetro saltarFinDeSemana en True. Si por el contrario, se desea quitar de
	 * consideración los días de fin de semana, entonces se deberá establecer el parámetro
	 * saltarFinDeSemana en False.<br><br>
	 * Si se desea tomar en cuenta los días de asueto (declarados en DateUtils.properties)
	 * , se deberá establecer el parámetro saltarDiasAsueto en True. Si por el contrario,
	 * se desea quitar de consideración lso días de asueto, entonces se deberá establecer
	 * el parámetro saltarDiasAsueto en False.
	 * @param fecha Fecha a partir d ela cual se calculará la Fecha de Liquidación.
	 * @param dias Días de vigencia (a transcurrir hasta la fecha de Liquidación).
	 * @param saltarFinDeSemana <br>
	 * <li>True: Si se considerarán los días de fin de semana 
	 * (Sábado y Domingo).</li>
	 * <li>False, si no se considerarán los días de fin de semana.</li>
	 * @param saltarDiasAsueto
	 * <li>True: Si se considerarán los días de fin de asueto
	 * (Declarados en el archivo DateUtils.properties).</li>
	 * <li>False si no se considerarán los días de asueto.</li>
	 * @return Objeto {@link Date} con la Fecha de Liquidación.
	 * @throws DateUtilsException En caso de error o problema con el calculo de la fecha
	 * de liquidación.
	 */
	public static Date fechaLiquidacion(Date fecha, int dias, boolean saltarFinDeSemana, boolean saltarDiasAsueto)
	throws DateUtilsException {
		Date    fVencimiento = null;
		int     ctaDias      = 0;
		boolean esDiaAsueto  = false;
		
		Calendar cfl = null;
		int diaSemana = -1;
		
		if(dias < 0)
			throw new DateUtilsException("El parámetro \"int dias\" tiene un valor no permitido");
		
		if(fecha == null)
			throw new DateUtilsException("El parametro \"Date fecha\" tiene un valor null no permitido");
		
		cfl = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cfl.setTime(fecha);
		
		while(ctaDias < dias){
			cfl.add(Calendar.DATE, 1); //Incrementa "fecha" en un día.
			fVencimiento = cfl.getTime();
			if(saltarFinDeSemana){
				diaSemana = cfl.get(Calendar.DAY_OF_WEEK);
				switch(diaSemana){
				case Calendar.SATURDAY:
				case Calendar.SUNDAY:
					continue;
				default:
					esDiaAsueto = DateUtils.esDiaAsueto(fVencimiento);
					if(esDiaAsueto)
						continue;
					ctaDias++;
				}
			} else {
				if (saltarDiasAsueto) {
					esDiaAsueto = DateUtils.esDiaAsueto(fVencimiento);
					if (esDiaAsueto){
						continue;
					}
				}
				ctaDias++;
			}			
		}
		
		return fVencimiento;
	}
	
	/**Metodo para obtener el calculo de la fecha de liquidación. Este método considera
	 * el cálculo sin tomar en cuenta los días de fin de semana (Sábado y Domingo) y sin
	 * considerar días de asueto. Estos últimos se pueden leer desde un archivo
	 * DateUtils.properties, o bien, desde una tabla en la base de datos de Scrittura.
	 * @param Objeto {@link Connection} para enlazar a la base de datos.
	 * @param fecha Fecha a partir de la cual se caculará la de Liquidación.
	 * @param dias Dias a partir de la fecha de liquidacion para obtener el vencimiento
	 * @return Objeto {@link Date} Fecha de Vencimiento.
	 * @throws DateUtilsException Cuando ocurre un problema con el calculo de la fecha
	 * de liquidación.
	 */
	public static Date fechaLiquidacion(Connection conn, Date fecha, int dias)
	throws DateUtilsException {
		return DateUtils.fechaLiquidacion(conn, fecha, dias, false, false);
	}
	
	/**Método para obtener el cálculo de la fecha de liquidación. Este método considera
	 * el cálculo sin tomar los días de asueto. Se puede decidir a voluntad si se desea
	 * considerar los días de fin de semana (Sábado y domingo), se puede habilitar el
	 * parámetro quitarFinDeSemana en True. Si se desea quitar de consideración los días
	 * de fin de semana del cálculo, simplemente se deberá establecer el parámetro
	 * quitarFinDeSemana en False.
	 * @param conn Objeto {@link Connection} para enlace a la base de datos.
	 * @param fecha Fecha a partir de la cual se calculará la Fecha de Liquidación.
	 * @param dias
	 * @param quitarFinDeSemana
	 * @return Objeto {@link Date} con la Fecha de liquidación.
	 * @throws DateUtilsException En caso de error o problema con el calculo de la fecha
	 * de liquidación.
	 */
	public static Date fechaLiquidacion(Connection conn, Date fecha, int dias, boolean saltarFinDeSemana)
	throws DateUtilsException {
		return DateUtils.fechaLiquidacion(conn, fecha, dias, saltarFinDeSemana, false);
	}
	
	/**Método para obtener el calculo de la fecha de liquidación de otra fecha dada.
	 * @param conn Objeto {@link Connection} para el enlace a la base de datos.
	 * @param fecha Objeto {@link Date} a partir del cual se obtendrá la fecha de vencimiento.
	 * @param dias Días de vigencia.
	 * @param saltarFinDeSemana
	 * <li>True: si se descartarán los días de fin de semana.
	 * <li>False: si NO se descartarán los días de fin de semana.
	 * @param saltarDiasAsueto
	 * <li>True: si se descartarán los días de asueto.
	 * <li>False: si NO se descartarán los días de asueto.
	 * @return Objeto {@link Date} con la Fecha de Liquidación.
	 * @throws DateUtilsException En caso de fracaso con el cálculo de la fecha de liquidación.
	 */
	public static Date fechaLiquidacion(Connection conn, Date fecha, int dias, boolean saltarFinDeSemana, boolean saltarDiasAsueto)
	throws DateUtilsException{
		Date fVencimiento   = null;
		int  i              = 0;
		int  ctaDias        = 0;
		boolean esDiaAsueto = false;
		
		Calendar cfl = null; //Objeto calendar representando la Fecha de Liquidación.
		int diaSemana = -1;
		
		if(conn == null)
			throw new DateUtilsException("El parametro \"Connection conn\" tiene un valor null no permitido.");
		
		if(fecha == null)
			throw new DateUtilsException("El parametro \"Date fecha\" tiene un valor null no permitido.");
		
		if(dias < 0)
			throw new DateUtilsException("El parametro dias tiene un valor no permitido.");
		
		cfl = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cfl.setTime(fecha);
		
		for(i=0, ctaDias = 0; ctaDias < dias; i++){
			
			cfl.add(Calendar.DATE, 1); //Incrementa "fecha" en un día.
			fVencimiento = cfl.getTime();

			if(saltarFinDeSemana){
				diaSemana = cfl.get(Calendar.DAY_OF_WEEK);
				switch(diaSemana){
				case Calendar.SATURDAY:
				case Calendar.SUNDAY:
					continue;
				default:
					
					if (saltarDiasAsueto) {
						esDiaAsueto = DateUtils.esDiaAsueto(conn, fVencimiento, DateUtils.UBICACION_MEXICO);
						if (esDiaAsueto){
							continue;
						}
					}
					ctaDias++;
				}
			} else {
				if (saltarDiasAsueto) {
					esDiaAsueto = DateUtils.esDiaAsueto(conn, fVencimiento, DateUtils.UBICACION_MEXICO);
					if (esDiaAsueto){
						continue;
					}
				}
				ctaDias++;
			}			
		}
		
		return fVencimiento;
	}
	
	public static Date fechaVencimiento(Date fecha, int diasVencimiento, boolean esVigenciaNatural){
		Date vencimiento = null;
		Date fechaAux = null;
		
		vencimiento = new Date(fecha.getTime());
		fechaAux = new Date(fecha.getTime());
		
		int dia = -1;
		int mes = -1;
		int anio = -1;
		
		GregorianCalendar c = new GregorianCalendar();
		
		if(diasVencimiento == 30 && (!esVigenciaNatural) ) {
			
			vencimiento = DateUtils.addMonth(vencimiento, 1);
			dia = DateUtils.getDia(fechaAux);
			mes = DateUtils.getMes(fechaAux);
			anio = DateUtils.getAnio(fechaAux);
			
			if(c.isLeapYear(anio)) {
				if( !(mes == DateUtils.ENERO && dia > 29) ){
					vencimiento = DateUtils.addDay(vencimiento, -1);
				} 
			} else {
				if( !(mes == DateUtils.ENERO && dia > 28)) {
					vencimiento = DateUtils.addDay(vencimiento, -1);
				}
			}
		} else {
			vencimiento = DateUtils.addDay(vencimiento, diasVencimiento);
		}
		
		return vencimiento;
	}
	
	/**Obtiene el día laboral anterior a la fecha proporcionada en el parametro.
	 * @param fecha Fecha para la cual se desea obtener el día laboral anterior.
	 * @param dias Numero de dias a recorrer hasta obtener la fecha laboral anterior.
	 * @param saltarFinDeSemana True, si desea saltar los fines de semana.
	 * @param saltarDiasAsueto True, si desea saltar los dias de asueto.
	 * @return
	 * @throws DateUtilsException
	 */
	public static Date getFechaLaboralAnterior(Date fecha, int dias, boolean saltarFinDeSemana, boolean saltarDiasAsueto)
	throws DateUtilsException {
		Date    fVencimiento = null;
		int     ctaDias      = 0;
		boolean esDiaAsueto  = false;
		
		Calendar cfl = null;
		int diaSemana = -1;
		
		if(dias < 0)
			throw new DateUtilsException("El parámetro \"int dias\" tiene un valor no permitido");
		
		if(fecha == null)
			throw new DateUtilsException("El parametro \"Date fecha\" tiene un valor null no permitido");
		
		if(dias > 0){
			dias = dias * -1;
		}
		
		
		cfl = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cfl.setTime(fecha);
		
		while(ctaDias > dias){
			cfl.add(Calendar.DATE, -1); //Decrementa "fecha" en un día.
			fVencimiento = cfl.getTime();
			if(saltarFinDeSemana){
				diaSemana = cfl.get(Calendar.DAY_OF_WEEK);
				switch(diaSemana){
				case Calendar.SATURDAY:
				case Calendar.SUNDAY:
					continue;
				default:
					esDiaAsueto = DateUtils.esDiaAsueto(fVencimiento);
					if(esDiaAsueto)
						continue;
					ctaDias--;
				}
			} else {
				if (saltarDiasAsueto) {
					esDiaAsueto = DateUtils.esDiaAsueto(fVencimiento);
					if (esDiaAsueto){
						continue;
					}
				}
				ctaDias++;
			}			
		}
		
		return fVencimiento;
	}
	
	/**Determina si el parametro {@link Date} es un dia festivo, a partir de
	 * la definición que existe en el archivo DateUtils.properties.
	 * @param fecha Objeto {@link Date} del cual se desea determinar si es dia de asueto.
	 * @return
	 * <li>True: Si el día es de asueto.
	 * <li>False: Si el día NO es de asueto.
	 * @throws DateUtilsException Cuando se presenta un problema para determinar la fecha
	 * de asueto.
	 */
	public static boolean esDiaAsueto(Date fecha)
	throws DateUtilsException {
		boolean    resultado  = false;
		Properties conf       = null;
		Map        mapDias    = null;
		int        mes        = -1;
		String     key        = null;
		String     value      = null;
		String[]   strDias    = null;
		int        i          = 0;
		int        dia        = -1;
		Date       fEspecial  = null;
		int        diferencia = 0;
		
			try {
				//Carga la configuración del archivo .properties
				conf = DateUtils.readConfiguration("DateUtils.properties");
				
				//Carga los registros de configuración correspondientes al patrón "fecha.asueto"
				mapDias = DateUtils.loadMappers(DateUtils.PREF_FECHA + "." + DateUtils.PREF_ASUETO, conf);
				mes = DateUtils.getMes(fecha); //Obtiene el mes del parametro fecha en formato numerico
				key = DateUtils.getMes(mes); //Realiza la conversion del mes de numero a letra.
				
				value = (String) mapDias.get(key);
				
				//Verifica del archivo DateUtils.properties los días considerados como fijos en cada mes.
				if(value != null){
					value = value.trim();
					strDias = value.split(KEY_SEPARADOR_DIA);
					
					for(i = 0; i < strDias.length; i++) {
						dia = Integer.parseInt(strDias[i]);
						if(dia == DateUtils.getDia(fecha)){
							resultado = true;
							break;
						}
					}
				}
				
				//Verifica del archivo DateUtils.properties los días considerados como "Especiales".
				if(resultado == false) {
					strDias = null;
					value = (String) mapDias.get(PREF_DIA_ESPECIAL);
					
					if(value != null){
						strDias = value.trim().split(KEY_SEPARADOR_DIA); 
						for(i=0; i < strDias.length; i++){
							fEspecial = DateUtils.getDate(strDias[i], DateUtils.FORMATO_DD_MM_YYYY);
							diferencia = fecha.compareTo(fEspecial);
							if(diferencia == 0 ){
								resultado = true;
								break;
							}
						}
					}
				}
			} catch (NumberFormatException ex) {
				throw new DateUtilsException("El dia se encuentra en formato incorrecto.", ex);
			} catch (IOException ex) {
				
			} finally {
				
			}
		return resultado;		
	}
	
	/**Determina si el parámetro {@link Date} es un día de asueto, a partir de la definición
	 * que existe en la Base de Datos de Scrittura, en la tabla donde se dan de alta los días de asueto.
	 * @param conn Objeto {@link Connection} para el enlace a la base de datos y ejecutar
	 * consultas a la misma.
	 * @param fecha Objeto {@link Date} del cual se desea saber si es un día de asueto.
	 * @param ubicacion Indica la ubicación de donde se desea saber las fechas de asueto.
	 * @return
	 * <li>True: si el parametro fecha es un día de asueto.
	 * <li>False: si el parámetro fecha NO es un día de asueto.
	 * @throws DateUtilsException Si ocurre un error al obtener la fecha de asueto.
	 */
	public static boolean esDiaAsueto(Connection conn, Date fecha, String ubicacion)
	throws DateUtilsException {
		boolean    resultado  = false;
		
		PreparedStatement psAsueto = null;
		ResultSet         rsAsueto = null;
		String            sqlAsueto = null;
		
		Date fechaIni = null;
		Date fechaFin = null;
		
			try {
				fechaIni = new Date(fecha.getTime());
				DateUtils.setHora(fechaIni, 0);
				DateUtils.setMinuto(fechaIni, 0);
				DateUtils.setSegundo(fechaIni, 0);
				DateUtils.setMilisegundo(fechaIni, 0);
				
				fechaFin = new Date(fecha.getTime());
				DateUtils.setHora(fechaFin, 23);
				DateUtils.setMinuto(fechaFin, 59);
				DateUtils.setSegundo(fechaFin, 59);
				DateUtils.setMilisegundo(fechaFin, 999);
				
				//TODO Por determinar la tabla final donde se alojarán las fechas de asueto.
				sqlAsueto = "select CALENDAR_YEAR, HOLIDAY_NAME, HOLIDAY_DATE, HOLIDAY_LOCATION, "
					+ "DATE_CREATED, DATE_UPDATED, DEFAULT_LOCATION from BMEA_IPITOOLS_HOLIDAYS "
					+ "where HOLIDAY_DATE between ? and ? and HOLIDAY_LOCATION = ?";
				psAsueto = conn.prepareStatement(sqlAsueto);
				psAsueto.setDate(1, new java.sql.Date(fechaIni.getTime()) );
				psAsueto.setDate(2, new java.sql.Date(fechaFin.getTime()) );
				psAsueto.setString(3, ubicacion);
				rsAsueto = psAsueto.executeQuery();
				
				if(rsAsueto.next())
					resultado = true;
				
			} catch (SQLException ex) {
				throw new DateUtilsException("Existe un problema para buscar los dias de asueto en la BD...", ex);
			} catch (NumberFormatException ex) {
				throw new DateUtilsException("El dia se encuentra en formato incorrecto.", ex);
			} finally {
				//Cierra el objeto PreparedStatement
				try{
					if(psAsueto != null){
						psAsueto.close();
					}
				} catch (SQLException ex){
					ex.printStackTrace();
				} finally {
					psAsueto = null;	
				}
				
				//Cierra el objeto ResultSet
				try{
					if(rsAsueto != null){
						rsAsueto.close();

					}
				} catch(SQLException ex) {
					ex.printStackTrace();
				} finally {
					rsAsueto = null;					
				}
			}
		return resultado;
	}
	
	/**Devuelve el año en formato numérico del objeto {@link Date} dado.
	 * @param fecha {@link Date} con la fecha.
	 * @return Representación numérica del año.
	 */
	public static int getAnio(Date fecha){
		int anio = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		anio = cal.get(Calendar.YEAR);
		
		return anio;
	}
	
	/**Devuelve la representación del mes en un formato String a partir de su
	 * representación Numérica. Vea las constantes de mes para la clase {@link DateUtils}
	 * @param mes Representación Numérica del mes [0-11],<br>
	 * Donde:<br>
	 * 	<li>0 = "Enero"</li>
	 * 	<li>1 = "Febrero"</li>
	 * 	<li>2 = "Marzo"</li>
	 * 	<li>3 = "Abril"</li>
	 *	<li>etc...</li><br>
	 * Vea también las <i>constantes de mes</i> de la clase {@link DateUtils}
	 * @return Representación en un objeto {@link String} del mes proporcionado
	 * en el parámetro <strong>int mes</strong>.
	 * @throws DateUtilsException Se devuelve cuando se introduce un valor
	 * incorrecto para el parámetro mes.
	 */
	public static String getMes(int mes)
	throws DateUtilsException{
		String strMes = null;
		switch(mes){
		case ENERO: strMes = PROP_ENERO; break;
		case FEBRERO: strMes = PROP_FEBRERO; break;
		case MARZO: strMes = PROP_MARZO; break;
		case ABRIL: strMes = PROP_ABRIL; break;
		case MAYO: strMes = PROP_MAYO; break;
		case JUNIO: strMes = PROP_JUNIO; break;
		case JULIO: strMes = PROP_JULIO; break;
		case AGOSTO: strMes = PROP_AGOSTO; break;
		case SEPTIEMBRE: strMes = PROP_SEPTIEMBRE; break;
		case OCTUBRE: strMes = PROP_OCTUBRE; break;
		case NOVIEMBRE: strMes = PROP_NOVIEMBRE; break;
		case DICIEMBRE: strMes = PROP_DICIEMBRE; break;
		default:
			throw new DateUtilsException("El mes no es válido: " + mes);
		}
		return strMes;
	}
	
	/**Devuelve el mes en formato numérico [0-11] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Mes en formato numérico [0-11] del objeto. Vea también las <i>constantes
	 * de mes</i> de la clase {@link DateUtils}
	 */
	public static int getMes(Date fecha){
		int mes = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		mes = cal.get(Calendar.MONTH);
		
		return mes;
	}
	
	/**Devuelve el dia, en formato numérico [1-31] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Dia en formato numérico [1-31]. El rango puede variar, según sea el mes
	 * que se haya establecido en el parámetro de entrada.
	 */
	public static int getDia(Date fecha){
		int dia = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		dia = cal.get(Calendar.DATE);
		
		return dia;
	}

	/**Devuelve la hora, en formato numérico [0-23] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Hora en formato numérico [0-23].
	 */
	public static int getHora(Date fecha) {
		int hora = -1;		
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		hora = cal.get(Calendar.HOUR_OF_DAY);
		
		return hora;
	}
	
	/**Devuelve los minutos, en formato numérico [0-59] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Minutos en formato numérico [0-59].
	 */
	public static int getMinuto(Date fecha){
		int minuto = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		minuto = cal.get(Calendar.MINUTE);
		
		return minuto;
	}
	
	/**Devuelve los segundos, en formato numérico [0-59] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Segundos en formato numérico [0-59].
	 */
	public static int getSegundo(Date fecha){
		int segundo = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		segundo = cal.get(Calendar.SECOND);
		
		return segundo;
	}
	
	/**Devuelve los milisegundos en formato numérico [0-999] del objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} con la fecha.
	 * @return Milisegundos en formato numérico [0-999].
	 */
	public static int getMilisegundo(Date fecha) {
		int milisegundo = -1;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		milisegundo = cal.get(Calendar.MILLISECOND);
		
		return milisegundo;
	}
	
	/**Establece valor del año para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para el año.
	 * @param anio El nuevo valor para el Año que se establecerá en el parametro {@link Date}.
	 */
	public static void setAnio(Date fecha, int anio){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.YEAR, anio);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor del mes para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para el mes.
	 * @param mes El nuevo valor para el mes que se establecerá en el parámetro {@link Date}.
	 * Vea las constantes de la clase {@link DateUtils} para establecer correctamente el mes.
	 */
	public static void setMes(Date fecha, int mes){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.MONTH, mes);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor pdel día para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para el dia.
	 * @param dia El nuevo valor para el día que se establecerá en el parámetro {@link Date}.
	 */
	public static void setDia(Date fecha, int dia){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.DAY_OF_MONTH, dia);
		
		fecha.setTime(cal.getTimeInMillis());
		
	}
	
	/**Establece el valor de la hora para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para la hora.
	 * @param hora El nuevo valor para la hora que se establecerá en el parámetro {@link Date}.
	 */
	public static void setHora(Date fecha, int hora){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR_OF_DAY, hora);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor de los minutos para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para los minutos.
	 * @param minuto El nuevo valor para los minutos que se establecerá en el parámetro {@link Date}.
	 */
	public static void setMinuto(Date fecha, int minuto){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.MINUTE, minuto);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor de los segundos para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para los minutos.
	 * @param segundo El nuevo valor para los segundos que se establecerá en el parámetro {@link Date}.
	 */
	public static void setSegundo(Date fecha, int segundo){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.SECOND, segundo);

		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Establece el valor de los milisegundos para el objeto {@link Date} dado.
	 * @param fecha Objeto {@link Date} al cual se desea establecer el nuevo valor para los minutos.
	 * @param milisegundo El nuevo valor para los milisegundos que se establecerá en el parámetro {@link Date}.
	 */
	public static void setMilisegundo(Date fecha, int milisegundo){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.MILLISECOND, milisegundo);

		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Devuelve un objeto {@link Date} con el día, mes y año que se especifiquen.
	 * @param year Año en formato numérico (int).
	 * @param month Mes en formato numérico (int) [0-11]. Vea también las constantes de mes
	 * para la clase {@link DateUtils}.
	 * @param date Dia en formato numérico (int) [1-31].
	 * @return Objeto {@link Date} con la fecha establecida en los parámetros.
	 */
	public static Date getDate(int year, int month, int date){
		Date     fecha = null;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.set(year, month, date);
		fecha = cal.getTime();
		
		return fecha;
	}
	
	public static Date getDate(int year, int month, int date, int hour, int minute, int second){
		Date     fecha = null;
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.set(year, month, date, hour, minute, second);
		fecha = cal.getTime();
		
		return fecha;
	}
	
	public static void setTime(Date fecha, int hour, int minute, int second){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	public static void setTime(Date fecha, int hour, int minute, int second, int millisecond){
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	public static void setTime(Date fecha, int hour, int AM_PM, int minute, int second, int millisecond) {
		Calendar cal = null;
		
		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
		cal.setTime(fecha);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.AM_PM, AM_PM);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		
		fecha.setTime(cal.getTimeInMillis());
	}
	
	/**Metodo que establece la hora en 00:00:00.00 de una fecha dada. 
	 * @param fecha
	 */
	public static void resetTime(Date fecha){
		setTime(fecha, 0, 0, 0, 0);
	}
	
	/**Método para obtener un {@link String} en un {@link Date}. La cadena de entrada
	 * debe contener una fecha con el formato dd/MM/yyyy<br>
	 * Donde:
	 * <li>dd  : Dia (En formato numérico [0-9])</li>
	 * <li>MM  : Mes (En formato numérico [01-12], donde 01 = Enero, 02 = Febrero, ...)</li>
	 * <li>yyyy: Año (En formato numérico de cuatro dígitos).</li><br><br>
	 * @param strFecha {@link String} con la cadena que representa la fecha.
	 * @return Objeto {@link Date} con la representación de la cadena.
	 */
	public static Date getDate(String strFecha, String formato) {
		Date             fecha = null;
		SimpleDateFormat dateFormat = null;
		Calendar cal = null;
		try{
			dateFormat = new SimpleDateFormat(formato);
			fecha = dateFormat.parse(strFecha);
			cal = new GregorianCalendar(TimeZone.getTimeZone("GMT-6:00"), Locale.getDefault());
			cal.setTime(fecha);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			fecha = cal.getTime();
		} catch( ParseException ex) {
			ex.printStackTrace();
		}
		return fecha;
	}
	
	/**Metodo para respresentar un objeto {@link Date} en un {@link String} con
	 * el formato dd/MM/yyyy (o en cualquier otro orden).<br>
	 * Donde:<br>
	 * <ul>
	 * <li>   dd  : Dia (En formato numérico [0-9])</li>
	 * <li>   MM  : Mes (En formato numérico [01-12], donde 01 = Enero, 02 = Febrero, ...)</li>
	 * <li>   yyyy: Año (En formato numérico de cuatro dígitos)</li><br><br>
	 * </ul>
	 * @param fecha Objeto {@link Date}
	 * @return Objeto {@link String} con la representación en texto del objeto {@link Date}
	 */
	public static String getString(Date fecha, String formato)
	throws DateUtilsException {
		String           strFecha   = null;
		SimpleDateFormat dateFormat = null;
		
		dateFormat = new SimpleDateFormat(formato);
		if(fecha == null)
			throw new DateUtilsException("El parámetro fecha no debe ser nulo");
		strFecha = dateFormat.format(fecha);
		
		return strFecha;
	}
	
	/**Devuelve en base a la fecha proporcionada, el último día del mes.
	 * @param fecha
	 * @return
	 */
	public static Date getLastDayOfMonth(Date fecha){
		Date lastDayOfMonth = null;
		GregorianCalendar cal = null;
		int mes = -1;
		int lastDay = -1;
		
		cal = new GregorianCalendar();
		cal.setTime(fecha);
		mes = cal.get(Calendar.MONTH);
		
		switch(mes){
		case ENERO: lastDay = 31; break;
		case FEBRERO:
			if(cal.isLeapYear(cal.get(GregorianCalendar.YEAR)))
				lastDay = 29;
			else
				lastDay = 28;
			break;
		case MARZO: lastDay = 31; break;
		case ABRIL: lastDay = 30; break;
		case MAYO: lastDay = 31; break;
		case JUNIO: lastDay = 30; break;
		case JULIO: lastDay = 31; break;
		case AGOSTO: lastDay = 31; break;
		case SEPTIEMBRE: lastDay = 30; break;
		case OCTUBRE: lastDay = 31; break;
		case NOVIEMBRE: lastDay = 30; break;
		case DICIEMBRE: lastDay = 31; break;
		}
		
		cal.set(GregorianCalendar.DAY_OF_MONTH, lastDay);
		
		lastDayOfMonth = new Date(cal.getTimeInMillis());
		
		return lastDayOfMonth;
	}
	
	public static Date getFirstDayOfMonth(Date fecha){
		Date firstDayOfMonth = null;
		GregorianCalendar cal = null;
		int firstDay = 1;
		
		cal = new GregorianCalendar();
		cal.setTime(fecha);
		cal.set(GregorianCalendar.DAY_OF_MONTH, firstDay);
		firstDayOfMonth = new Date(cal.getTimeInMillis());
		
		return firstDayOfMonth;
	}
	
	/**Formatea un valor de tiempo en Milisegundos
	   * @param elapsedTimeMillis Tiempo a formatear
	   * @return Tiempo formateado
	   */
	  public static String formatElapsedTime(long elapsedTimeMillis) {

	    SimpleDateFormat dateFormat = new SimpleDateFormat("H 'hrs' m 'min' s 'seg' SSS 'ms'");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return dateFormat.format(new Date(elapsedTimeMillis));
	  }
	  
	/**Devuelve la diferencia en dias de dos fechas. De preferencia, el primer parametro (fechaIni)
	 * debera ser la fecha menor, mientras que el segundo parametro debera ser la fecha mayor.
	 * @param fechaIni
	 * @param fechaFin
	 * @return dias de diferencia.
	 */
	public static int daysDiff(Date fechaIni, Date fechaFin) {
		int days = 0;
        long dayIni = 0L;
        long dayFin = 0L;
        double daysD = 0.0D;
        BigDecimal bdDias = null;
        dayIni = fechaIni.getTime();
        dayFin = fechaFin.getTime();
        daysD = (double)(dayFin - dayIni) / (double)(24 * 60 * 60 * 1000);
        
        bdDias = new BigDecimal(daysD);
        bdDias = bdDias.setScale(1, BigDecimal.ROUND_HALF_UP);
        days = bdDias.intValue();
        days++;
        
        return days;
		
	}
	
	public static boolean isDateBetween(Date fecha, Date fechaIni, Date fechaFin){
		boolean resultado = false;
		
		long lFecha = fecha.getTime();
		long lFechaIni = fechaIni.getTime();
		long lFechaFin = fechaFin.getTime();
		
		if(lFecha >= lFechaIni && lFecha <= lFechaFin)
			resultado = true;
		
		return resultado;
	}
	
	public static Integer getDiaSemana(Date fecha) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT-6:00"), new Locale("es", "MX"));
		Integer diaSemana = null;
		
		cal.setTime(fecha);
		diaSemana = new Integer(cal.get(Calendar.DAY_OF_WEEK));
		
		return diaSemana;
	}
}