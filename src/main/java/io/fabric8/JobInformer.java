package io.fabric8;

import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;

import java.util.logging.Logger;

public class JobInformer {
    private static final Logger logger = Logger.getLogger(JobInformer.class.getName());

    public static void main(String[] args) throws InterruptedException {
        try (KubernetesClient client = new DefaultKubernetesClient()) {

            // Create namespaced instance for Job Informer
            SharedIndexInformer<Job> jobSharedIndexInformer = client.batch().v1().jobs()
                    .inNamespace("rokumar-dev")
                    .inform(new ResourceEventHandler<Job>() {
                        @Override
                        public void onAdd(Job job) {
                            logger.info("Job " + job.getMetadata().getName() + " got added");
                        }

                        @Override
                        public void onUpdate(Job oldJob, Job newJob) {
                            logger.info("Job " + oldJob.getMetadata().getName() + " got updated");
                        }

                        @Override
                        public void onDelete(Job job, boolean deletedFinalStateUnknown) {
                            logger.info("Job " + job.getMetadata().getName() + " got deleted");
                        }
                    }, 30 * 1000L);
            logger.info("Informer factory initialized.");


            logger.info("Starting all registered informers");

            // Wait for 1 minute
            Thread.sleep(60 * 1000L);
        }
    }
}
