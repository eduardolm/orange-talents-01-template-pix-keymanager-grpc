rm -vf key_manager_grpc_docker.zip
zip -r key_manager_grpc_docker.zip *
aws s3 cp key_manager_grpc_docker.zip s3://pix-bucket-31032021/

rm -vf key_manager_rest_docker.zip
zip -r key_manager_rest_docker.zip *
aws s3 cp key_manager_rest_docker.zip s3://pix-bucket-31032021/

rm -vf bcb_pix_docker.zip
zip -r bcb_pix_docker.zip *
aws s3 cp bcb_pix_docker.zip s3://pix-bucket-31032021/

rm -vf itau_erp_docker.zip
zip -r itau_erp_docker.zip *
aws s3 cp itau_erp_docker.zip s3://pix-bucket-31032021/