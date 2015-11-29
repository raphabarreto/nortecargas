package br.com.nortecargas.mb;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;

import br.com.nortecargas.model.Cliente;
import br.com.nortecargas.service.ClienteService;

@ManagedBean(name="clienteMB")
@ViewScoped
public class ClienteMB
implements Serializable {
    private static final long serialVersionUID = 1;
    @EJB
    private ClienteService service;
    private Long idSelecionado;
    private List<Cliente> clientes;
    private Cliente cliente;

    public void setIdSelecionado(Long idSelecionado) {
        this.idSelecionado = idSelecionado;
    }

    public Long getIdSelecionado() {
        return this.idSelecionado;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void incluir() {
        this.cliente = new Cliente();
    }

    public void editar() {
        if (this.idSelecionado == null) {
            return;
        }
        this.cliente = (Cliente)this.service.find(this.idSelecionado);
    }

    public List<Cliente> getClientes() {
        if (this.clientes == null) {
            this.clientes = this.service.findAll();
        }
        return this.clientes;
    }

    public void setClientes(List<Cliente> Clientes) {
        this.clientes = Clientes;
    }

    public String salvar() {
        try {
            this.service.save(this.cliente);
        }
        catch (Exception ex) {
            this.addMessage(this.getMessageFromI18N("msg.erro.salvar.cliente"), ex.getMessage());
            return "";
        }
        return "listaClientes";
    }

    public String remover() {
        try {
            this.service.remove(this.cliente);
        }
        catch (Exception ex) {
            this.addMessage(this.getMessageFromI18N("msg.erro.remover.cliente"), ex.getMessage());
            return "";
        }
        return "listaClientes";
    }

    private String getMessageFromI18N(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        return bundle.getString(key);
    }

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
    }
    
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);
 
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "img" + File.separator + "logo.png";
         
        pdf.add(Image.getInstance(logo));
    }
}
