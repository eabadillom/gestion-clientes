package com.ferbo.clientes.model;

import java.util.List;

import org.primefaces.omega.domain.InventoryStatus;
import org.primefaces.omega.domain.Order;

public class StatusFactura {
	
	public static final int STATUS_ERROR = 0;
	public static final int STATUS_POR_COBRAR = 1;
	public static final int STATUS_CANCELADA = 2;
	public static final int STATUS_PAGADA = 3;
	public static final int STATUS_PAGO_PARCIAL = 4;

	  private InventoryStatus inventoryStatus;
	    private List<Order> orders;
	    
	private Integer id;
    private String nombre;
    private String descripcion;
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public static int getStatusError() {
		return STATUS_ERROR;
	}
	public static int getStatusPorCobrar() {
		return STATUS_POR_COBRAR;
	}
	public static int getStatusCancelada() {
		return STATUS_CANCELADA;
	}
	public static int getStatusPagada() {
		return STATUS_PAGADA;
	}
	public static int getStatusPagoParcial() {
		return STATUS_PAGO_PARCIAL;
	}
	public InventoryStatus getInventoryStatus() {
		return inventoryStatus;
	}
	public void setInventoryStatus(InventoryStatus inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

    
}
