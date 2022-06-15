package business.trialsTypes;

import business.TrialTypeOptions;

/**
 * Representa el tipo de prueba Budget
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class Budget extends GenericTrial{
    private String nameEntity;
    private int amount;

    /**
     * Método constructor que crea un nuevo artículo, teniendo en cuenta si está en uso
     * @param nameTrial Nombre de la prueba
     * @param nameEntity Nombre del campo de estudio
     * @param amount Dificultad de la defensa de la tesis
     * @param inUse Nos permitirá saber si la prueba se usa en alguna edición
     */
    public Budget(String nameTrial, String nameEntity, int amount, boolean inUse) {
        super(nameTrial, TrialTypeOptions.BUDGET, inUse);
        this.nameEntity = nameEntity;
        this.amount = amount;
    }

    /**
     * Método que obtiene el nombre de la prueba
     * @return String con el nombre de la prueba
     */
    public String getNameTrial() {
        return super.getName();
    }

    /**
     * Método que nos permite saber si la prueba esta en uso
     * @return true si está en uso, false si no lo está
     */
    public boolean isInUse () {
        return super.getInUse();
    }

    /**
     * Método que obtiene el nombre de la entidad
     * @return
     */
    public String getNameEntity() {
        return nameEntity;
    }

    /**
     * Método que obtiene el monto de la prueba
     * @return Entero con el monto de la prueba
     */
    public int getAmount() {
        return amount;
    }

    public void setUsage(boolean use) {
        super.setUsage(use);
    }
}
