ClauseClear is a Spring Boot backend service designed to extract, chunk, and summarize large legal documents efficiently.
It leverages Apache Tika for robust text extraction from various document formats and Hugging Face’s transformers API for natural language summarization.
The service is tailored to process legal texts with attention to detail, helping legal professionals quickly digest long documents.

Features
File Upload: Accepts multi-format legal documents (PDF, DOCX, TXT, etc.) via multipart upload.

Text Extraction: Uses Apache Tika to reliably extract raw text content from uploaded files.

Chunking: Splits extracted text into manageable chunks (~1500 characters) for processing to avoid truncation issues.

Summarization: Calls Hugging Face’s facebook/bart-large-cnn model inference API to summarize each text chunk.

Input & Summary Pairing: Returns original text chunks alongside their summaries for easy comparison.

Error Handling: Handles and logs extraction or API errors gracefully.

