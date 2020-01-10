package io.fabric8;

import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Find Age of a project in Openshift
 */
public class FindAgeOfProject {
    private static final Logger logger = Logger.getLogger(FindAgeOfProject.class.getName());

    public static void main(String args[]) {

        try (OpenShiftClient openshiftClient = new DefaultOpenShiftClient()) {
            Project project = openshiftClient.projects().withName("myproject").get();

            System.out.println(project.getMetadata().getCreationTimestamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date creationTimestamp = simpleDateFormat.parse(project.getMetadata().getCreationTimestamp());
            Date currentTimestamp = simpleDateFormat.parse(simpleDateFormat.format(new Date()));

            long diffInMillis = Math.abs(currentTimestamp.getTime() - creationTimestamp.getTime());
            
            long days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            long hours = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            long minutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
            long seconds = TimeUnit.SECONDS.convert(diffInMillis, TimeUnit.MILLISECONDS);

            logger.log(Level.INFO, "Project is " + days + " days," +
                    hours + " hours," +
                    minutes + "minutes," +seconds + "seconds old.");

        } catch (KubernetesClientException | ParseException aException) {
            logger.log(Level.SEVERE, "Problem encountered in Kubernetes Client");
            aException.printStackTrace();
        }
    }

}
