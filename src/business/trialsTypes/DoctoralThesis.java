package business.trialsTypes;

import business.TrialTypeOptions;

/**
 * Representa el tipo de prueba Doctoral
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class DoctoralThesis extends GenericTrial{
    private String fieldOfStudy;
    private int difficulty;

    /**
     * Método constructor que crea un nuevo DoctoralThesis, teniendo en cuenta si está en uso
     * @param name Nombre de la prueba
     * @param fieldOfStudy Nombre del campo de estudio
     * @param difficulty Dificultad de la defensa de la tesis
     * @param inUse Nos permitirá saber si la prueba se usa en alguna edición
     */
    public DoctoralThesis(String name, String fieldOfStudy, int difficulty, boolean inUse) {
        super(name, TrialTypeOptions.DOCTORAL, inUse);
        this.fieldOfStudy = fieldOfStudy;
        this.difficulty = difficulty;
    }

    /**
     * Método que nos permite saber el nombre de la prueba
     * @return El nombre de la prueba
     */
    public String getName() {
        return super.getName();
    }

    /**
     * Método que nos permite saber el nombre del campo de estudio
     * @return String con el nombre de campo de estudio
     */
    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    /**
     * Método que nos permite saber si la prueba esta en uso
     * @return true si está en uso, false si no lo está
     */
    public boolean isInUse() {
        return super.getInUse();
    }

    /**
     * Método que nos retorna la dificultad de presentar la tesis
     * @return Entero que indica la dificultad
     */
    public int getDifficulty() {
        return difficulty;
    }

    public void setUsage(boolean use) {
        super.setUsage(use);
    }
}