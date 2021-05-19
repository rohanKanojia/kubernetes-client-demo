package io.fabric8;

import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;

import java.util.logging.Logger;

public class JobInformer {
    private static final Logger logger = Logger.getLogger(JobInformer.class.getName());

    public static void main(String[] args) throws InterruptedException {
        try (KubernetesClient client = new DefaultKubernetesClient()) {
            // Get Informer Factory
            SharedInformerFactory sharedInformerFactory = client.informers();

            // Create namespaced instance for Job Informer
            SharedIndexInformer<Job> jobSharedIndexInformer = sharedInformerFactory
                    .sharedIndexInformerFor(Job.class,
                            new OperationContext().withNamespace("default"),
                            30 * 1000L);
            logger.info("Informer factory initialized.");

            // Add Event Handler for actions on all Job events received
            jobSharedIndexInformer.addEventHandler(
                    new ResourceEventHandler<Job>() {
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
                    }
            );

            logger.info("Starting all registered informers");
            sharedInformerFactory.startAllRegisteredInformers();

            // Wait for 1 minute
            Thread.sleep(60 * 1000L);
        }
    }
}
