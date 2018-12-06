package sns.sn.systems.pharmacist.consumption.model;

import sns.sn.systems.pharmacist.consumption.searchablespinner.Searchable;

public class Patient implements Searchable
{


    private String patientName;

    public Patient(String patientName) {
        this.patientName = patientName;
    }

    @Override
    public String getPatientName() {
        return patientName;
    }
}
