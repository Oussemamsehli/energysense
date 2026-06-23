output "vpc_id" {
  description = "ID of the VPC"
  value       = aws_vpc.energysense_vpc.id
}

output "s3_bucket_name" {
  description = "Name of the logs S3 bucket"
  value       = aws_s3_bucket.logs_bucket.bucket
}
