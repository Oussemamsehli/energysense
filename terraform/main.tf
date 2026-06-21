terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# VPC principal
resource "aws_vpc" "energysense_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name        = "energysense-vpc"
    Environment = var.environment
    Project     = "energysense"
  }
}

# Subnet public
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.energysense_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = false

  tags = {
    Name        = "energysense-public-subnet"
    Environment = var.environment
  }
}

# Security Group pour l'API
resource "aws_security_group" "api_sg" {
  name        = "energysense-api-sg"
  description = "Security group for EnergySense API"
  vpc_id      = aws_vpc.energysense_vpc.id

  ingress {
    description = "API port from VPC only"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "energysense-api-sg"
    Environment = var.environment
  }
}

# S3 bucket pour les logs
resource "aws_s3_bucket" "logs_bucket" {
  bucket = "energysense-logs-${var.environment}"

  tags = {
    Name        = "energysense-logs"
    Environment = var.environment
  }
}

# Bloquer l'accès public au bucket
resource "aws_s3_bucket_public_access_block" "logs_bucket_public_access" {
  bucket = aws_s3_bucket.logs_bucket.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

# Versioning S3
resource "aws_s3_bucket_versioning" "logs_bucket_versioning" {
  bucket = aws_s3_bucket.logs_bucket.id

  versioning_configuration {
    status = "Enabled"
  }
}

# Chiffrement S3
resource "aws_s3_bucket_server_side_encryption_configuration" "logs_bucket_encryption" {
  bucket = aws_s3_bucket.logs_bucket.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}
