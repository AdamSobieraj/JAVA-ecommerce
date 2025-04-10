CREATE TABLE cart (
  uuid UUID PRIMARY KEY,
  product_id UUID NOT NULL,
  quantity INTEGER NOT NULL,
  user_id UUID NOT NULL
);
